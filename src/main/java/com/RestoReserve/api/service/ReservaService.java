package com.RestoReserve.api.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.RestoReserve.api.dto.ReservaRequestDTO;
import com.RestoReserve.api.dto.ReservaResponseDTO;
import com.RestoReserve.api.exception.GlobalExceptionHandler.ResourceNotFoundException;
import com.RestoReserve.api.model.EstadoReserva;
import com.RestoReserve.api.model.Mesa;
import com.RestoReserve.api.model.Reserva;
import com.RestoReserve.api.repository.MesaRepository;
import com.RestoReserve.api.repository.ReservaRepository;
import com.RestoReserve.api.repository.UsuarioRepository;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private MesaRepository mesaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

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
            .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada"));
        return ReservaResponseDTO.fromEntity(reserva);
    }

    public ReservaResponseDTO crear(ReservaRequestDTO dto, String email) {
        Mesa mesa = mesaRepository.findById(dto.tableId())
            .orElseThrow(() -> new ResourceNotFoundException("Mesa no encontrada con id: " + dto.tableId()));

        var usuario = usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con email: " + email));

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
        reserva.setEstado(EstadoReserva.CANCELADA);
        reservaRepository.save(reserva);
    }

    public void eliminar(Long id) {
        if (!reservaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Reserva no encontrada");
        }
        reservaRepository.deleteById(id);
    }
}