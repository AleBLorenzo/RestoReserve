package com.RestoReserve.api.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.RestoReserve.api.model.Reserva;
import com.RestoReserve.api.model.Usuario;
import com.RestoReserve.api.repository.ReservaRepository;
import com.RestoReserve.api.repository.UsuarioRepository;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Reserva> listar() {
        return reservaRepository.findAll();
    }

    public Reserva obtenerPorId(Long id) {
        return reservaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Reserva no encontrada con id: " + id));
    }

    public List<Reserva> listarPorFecha(LocalDate fecha) {
        return reservaRepository.findByfecha(fecha)
            .map(List::of)
            .orElse(List.of());
    }

    public Reserva guardar(Reserva reserva) {
        if (reserva.getFecha() == null) {
            throw new IllegalArgumentException("La fecha es requerida");
        }
        if (reserva.getHorainicio() == null) {
            throw new IllegalArgumentException("La hora de inicio es requerida");
        }
        if (reserva.getHorafin() == null) {
            throw new IllegalArgumentException("La hora de fin es requerida");
        }
        if (reserva.getNumeropersonas() <= 0) {
            throw new IllegalArgumentException("El número de personas debe ser mayor a 0");
        }
        if (reserva.getUsuario() == null || reserva.getUsuario().getId() == null) {
            throw new IllegalArgumentException("El usuario es requerido");
        }
        
        Usuario usuario = usuarioRepository.findById(reserva.getUsuario().getId())
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        reserva.setUsuario(usuario);
        return reservaRepository.save(reserva);
    }

    public Reserva actualizar(Long id, Reserva reserva) {
        Reserva existente = obtenerPorId(id);
        existente.setFecha(reserva.getFecha());
        existente.setHorainicio(reserva.getHorainicio());
        existente.setHorafin(reserva.getHorafin());
        existente.setNumeropersonas(reserva.getNumeropersonas());
        existente.setEstado(reserva.getEstado());
        existente.setComentario(reserva.getComentario());
        return reservaRepository.save(existente);
    }

    public void eliminar(Long id) {
        if (!reservaRepository.existsById(id)) {
            throw new RuntimeException("Reserva no encontrada con id: " + id);
        }
        reservaRepository.deleteById(id);
    }
}