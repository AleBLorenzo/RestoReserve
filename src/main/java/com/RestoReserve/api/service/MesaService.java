package com.RestoReserve.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.RestoReserve.api.model.Mesa;
import com.RestoReserve.api.repository.MesaRepository;

@Service
public class MesaService {

    @Autowired
    private MesaRepository mesaRepository;

    public List<Mesa> listar() {
        return mesaRepository.findAll();
    }

    public Mesa obtenerPorId(Long id) {
        return mesaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Mesa no encontrada con id: " + id));
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

    public Mesa actualizar(Long id, Mesa mesaActualizada) {
        Mesa existente = obtenerPorId(id);
        existente.setNumeroDeMesa(mesaActualizada.getNumeroDeMesa());
        existente.setCapacidad(mesaActualizada.getCapacidad());
        return mesaRepository.save(existente);
    }

    public void eliminar(Long id) {
        if (!mesaRepository.existsById(id)) {
            throw new RuntimeException("Mesa no encontrada con id: " + id);
        }
        mesaRepository.deleteById(id);
    }
}