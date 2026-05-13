package com.RestoReserve.api.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.RestoReserve.api.dto.ReservaRequestDTO;
import com.RestoReserve.api.dto.ReservaResponseDTO;
import com.RestoReserve.api.exception.GlobalExceptionHandler.BadRequestException;
import com.RestoReserve.api.exception.GlobalExceptionHandler.ForbiddenException;
import com.RestoReserve.api.exception.GlobalExceptionHandler.ResourceNotFoundException;
import com.RestoReserve.api.model.EstadoPenalizacion;
import com.RestoReserve.api.model.EstadoReserva;
import com.RestoReserve.api.model.Mesa;
import com.RestoReserve.api.model.Reserva;
import com.RestoReserve.api.model.Usuario;
import com.RestoReserve.api.repository.MesaRepository;
import com.RestoReserve.api.repository.ReservaRepository;
import com.RestoReserve.api.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservaService {

    private static final int MARGEN_HORAS = 2;

    private final ReservaRepository reservaRepository;
    private final MesaRepository mesaRepository;
    private final UsuarioRepository usuarioRepository;

    public List<ReservaResponseDTO> listarTodos() {
        return reservaRepository.findAll().stream()
            .map(ReservaResponseDTO::fromEntity)
            .collect(Collectors.toList());
    }

    public List<ReservaResponseDTO> listarPorEmail(String email) {
        return reservaRepository.findByUsuarioEmail(email).stream()
            .map(ReservaResponseDTO::fromEntity)
            .collect(Collectors.toList());
    }

    public List<ReservaResponseDTO> listarPorEstado(EstadoReserva estado) {
        return reservaRepository.findByEstado(estado).stream()
            .map(ReservaResponseDTO::fromEntity)
            .collect(Collectors.toList());
    }

    public ReservaResponseDTO obtenerPorId(Long id) {
        Reserva reserva = reservaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada con id: " + id));
        return ReservaResponseDTO.fromEntity(reserva);
    }

    public ReservaResponseDTO crear(ReservaRequestDTO dto, String email) {
        // Validar que la fecha no sea en el pasado
        if (dto.fechahora().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("No se pueden crear reservas en el pasado");
        }

        Mesa mesa = mesaRepository.findById(dto.tableId())
            .orElseThrow(() -> new ResourceNotFoundException("Mesa no encontrada con id: " + dto.tableId()));

        var usuario = usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con email: " + email));

        if (usuario.getPenalizacion().equals(EstadoPenalizacion.BANNED)) {
            throw new ForbiddenException("User is banned due to multiple late cancellations");
        }
        // Validar capacidad
        if (dto.numeropersonas() > mesa.getCapacidad()) {
            throw new BadRequestException("El número de personas excede la capacidad de la mesa (" + mesa.getCapacidad() + ")");
        }

        // Validar franja horaria (±2 horas)
        LocalDateTime inicio = dto.fechahora().minusHours(MARGEN_HORAS);
        LocalDateTime fin = dto.fechahora().plusHours(MARGEN_HORAS);
        
        List<Reserva> reservasConflicto = reservaRepository.findReservasEnFranjaHoraria(
            mesa.getId(), inicio, fin
        );
        
        if (!reservasConflicto.isEmpty()) {
            throw new BadRequestException("Ya existe una reserva para esta mesa en la franja horaria de " + MARGEN_HORAS + " horas");
        }

        Reserva reserva = new Reserva();
        reserva.setFechahora(dto.fechahora());
        reserva.setNumeropersonas(dto.numeropersonas());
        reserva.setEstado(EstadoReserva.PENDIENTE);
        reserva.setComentario(dto.comentario());
        reserva.setMesas(java.util.Set.of(mesa));
        reserva.setUsuario(usuario);

        reserva = reservaRepository.save(reserva);
        return ReservaResponseDTO.fromEntity(reserva);
    }

    public ReservaResponseDTO actualizarEstado(Long id, EstadoReserva estado) {
        Reserva reserva = reservaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada"));
        reserva.setEstado(estado);
        reserva = reservaRepository.save(reserva);
        return ReservaResponseDTO.fromEntity(reserva);
    }

    public void cancelar(Long id) {
        Reserva reserva = reservaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada"));

        Usuario usuario = reserva.getUsuario();

        LocalDateTime now = LocalDateTime.now();

        LocalDateTime reservationTime = reserva.getFechahora();
        LocalDateTime limit = reservationTime.minusHours(2);

        if (now.isAfter(limit)) {
                usuario.setPenalizationPoints(usuario.getPenalizationPoints() + 2);
                if (usuario.getPenalizationPoints() >= 6) {
                    usuario.setPenalizacion(EstadoPenalizacion.BANNED);
                }
                usuarioRepository.save(usuario);  
          }

        reserva.setEstado(EstadoReserva.CANCELADA);
        reservaRepository.save(reserva);
    }

    public void eliminar(Long id) {
        if (!reservaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Reserva no encontrada con id: " + id);
        }
        reservaRepository.deleteById(id);
    }
}