package com.RestoReserve.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.RestoReserve.api.model.Mesa;
import com.RestoReserve.api.model.Reserva;
import com.RestoReserve.api.model.EstadoReserva;
import com.RestoReserve.api.repository.MesaRepository;
import com.RestoReserve.api.repository.ReservaRepository;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private MesaRepository mesaRepository;

    public List<Reserva> listarTodos() {
        return reservaRepository.findAll();
    }

    public List<Reserva> listarPorEstado(EstadoReserva estado) {
        return reservaRepository.findByEstado(estado);
    }

    public Reserva obtenerPorId(Long id) {
        return reservaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Reserva no encontrada con id: " + id));
    }

    public Reserva crear(Long mesaId, Reserva reserva) {
        Mesa mesa = mesaRepository.findById(mesaId)
            .orElseThrow(() -> new RuntimeException("Mesa no encontrada con id: " + mesaId));

        reserva.setEstado(EstadoReserva.PENDIENTE);
        reserva.setMesas(java.util.Set.of(mesa));

        return reservaRepository.save(reserva);
    }

    public Reserva actualizarEstado(Long id, EstadoReserva estado) {
        Reserva reserva = obtenerPorId(id);
        reserva.setEstado(estado);
        return reservaRepository.save(reserva);
    }

    public void eliminar(Long id) {
        if (!reservaRepository.existsById(id)) {
            throw new RuntimeException("Reserva no encontrada");
        }
        reservaRepository.deleteById(id);
    }
}