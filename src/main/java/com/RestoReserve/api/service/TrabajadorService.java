package com.RestoReserve.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.RestoReserve.api.model.Trabajador;
import com.RestoReserve.api.model.tipoEmpelado;
import com.RestoReserve.api.repository.TrabajadorRepository;

@Service
public class TrabajadorService {

    @Autowired
    private TrabajadorRepository trabajadorRepository;

    public List<Trabajador> listar() {
        return trabajadorRepository.findAll();
    }

    public Trabajador obtenerPorId(Long id) {
        return trabajadorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Trabajador no encontrado con id: " + id));
    }

    public Trabajador buscarPorNombreusuario(String nombreusuario) {
        return trabajadorRepository.findByNombreusuario(nombreusuario)
            .orElseThrow(() -> new RuntimeException("Trabajador no encontrado: " + nombreusuario));
    }

    public List<Trabajador> buscarPorTipo(tipoEmpelado tipo) {
        return trabajadorRepository.findByTipo(tipo)
            .map(List::of)
            .orElse(List.of());
    }

    public Trabajador guardar(Trabajador trabajador) {
        if (trabajador.getNombre() == null || trabajador.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es requerido");
        }
        if (trabajador.getNombreusuario() == null || trabajador.getNombreusuario().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de usuario es requerido");
        }
        if (trabajador.getContrasena() == null || trabajador.getContrasena().trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña es requerida");
        }
        if (trabajador.getTipo() == null) {
            throw new IllegalArgumentException("El tipo de empleado es requerido");
        }
        return trabajadorRepository.save(trabajador);
    }

    public Trabajador actualizar(Long id, Trabajador trabajador) {
        Trabajador existente = obtenerPorId(id);
        existente.setNombre(trabajador.getNombre());
        existente.setTipo(trabajador.getTipo());
        return trabajadorRepository.save(existente);
    }

    public void eliminar(Long id) {
        if (!trabajadorRepository.existsById(id)) {
            throw new RuntimeException("Trabajador no encontrado con id: " + id);
        }
        trabajadorRepository.deleteById(id);
    }
}