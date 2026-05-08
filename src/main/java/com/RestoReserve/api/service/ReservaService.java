package com.RestoReserve.api.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.RestoReserve.api.dto.ReservaRequestDTO;
import com.RestoReserve.api.dto.ReservaResponseDTO;
import com.RestoReserve.api.model.EstadoReserva;
import com.RestoReserve.api.model.Mesa;
import com.RestoReserve.api.model.Reserva;
import com.RestoReserve.api.model.Usuario;
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

    public List<ReservaResponseDTO> listarPorUsuario(Long usuarioId) {
        return reservaRepository.findByUsuarioId(usuarioId).stream()
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
            .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
        return ReservaResponseDTO.fromEntity(reserva);
    }

    public ReservaResponseDTO crear(ReservaRequestDTO dto, Long usuarioId) {
        Mesa mesa = mesaRepository.findById(dto.tableId())
            .orElseThrow(() -> new RuntimeException("Mesa no encontrada con id: " + dto.tableId()));

        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

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
            .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
        reserva.setEstado(estado);
        reserva = reservaRepository.save(reserva);
        return ReservaResponseDTO.fromEntity(reserva);
    }

    public void cancelar(Long id) {
        Reserva reserva = reservaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
        reserva.setEstado(EstadoReserva.CANCELADA);
        reservaRepository.save(reserva);
    }

    public void eliminar(Long id) {
        if (!reservaRepository.existsById(id)) {
            throw new RuntimeException("Reserva no encontrada");
        }
        reservaRepository.deleteById(id);
    }
}