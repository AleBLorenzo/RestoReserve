package com.RestoReserve.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.RestoReserve.api.model.Mesa;
import com.RestoReserve.api.model.Trabajador;
import com.RestoReserve.api.model.estadoMesa;
import com.RestoReserve.api.repository.MesaRepository;
import com.RestoReserve.api.repository.TrabajadorRepository;

@Service
public class MesaService {

    @Autowired
    private MesaRepository mesaRepository;

    @Autowired
    private TrabajadorRepository trabajadorRepository;

    public List<Mesa> listar() {
        return mesaRepository.findAll();
    }

    public Mesa obtenerPorId(Long id) {
        return mesaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Mesa no encontrada con id: " + id));
    }

    public Mesa obtenerPorNumero(int numeroDeMesa) {
        return mesaRepository.findByNumeroDeMesa(numeroDeMesa)
            .orElseThrow(() -> new RuntimeException("Mesa no encontrada con número: " + numeroDeMesa));
    }

    public Mesa guardar(Mesa mesa) {
        if (mesa.getNumeroDeMesa() <= 0) {
            throw new IllegalArgumentException("El número de mesa debe ser mayor a 0");
        }
        if (mesa.getCapacidad() <= 0) {
            throw new IllegalArgumentException("La capacidad debe ser mayor a 0");
        }
        return mesaRepository.save(mesa);
    }

    public Mesa actualizar(Long id, Mesa mesa) {
        Mesa existente = obtenerPorId(id);
        existente.setNumeroDeMesa(mesa.getNumeroDeMesa());
        existente.setCapacidad(mesa.getCapacidad());
        existente.setEstado(mesa.getEstado());
        return mesaRepository.save(existente);
    }

    public Mesa asignarTrabajador(Long mesaId, Long trabajadorId) {
        Mesa mesa = obtenerPorId(mesaId);
        Trabajador trabajador = trabajadorRepository.findById(trabajadorId)
            .orElseThrow(() -> new RuntimeException("Trabajador no encontrado"));
        mesa.setTrabajador(trabajador);
        return mesaRepository.save(mesa);
    }

    public Mesa cambiarEstado(Long id, estadoMesa estado) {
        Mesa mesa = obtenerPorId(id);
        mesa.setEstado(estado);
        return mesaRepository.save(mesa);
    }

    public void eliminar(Long id) {
        if (!mesaRepository.existsById(id)) {
            throw new RuntimeException("Mesa no encontrada con id: " + id);
        }
        mesaRepository.deleteById(id);
    }
}