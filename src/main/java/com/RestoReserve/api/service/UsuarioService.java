package com.RestoReserve.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.RestoReserve.api.model.Usuario;
import com.RestoReserve.api.repository.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    public Usuario obtenerPorId(Long id) {
        return usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
    }

    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado con email: " + email));
    }

    public Usuario guardar(Usuario usuario) {
        if (usuario.getNombre() == null || usuario.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es requerido");
        }
        if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("El email es requerido");
        }
        if (usuario.getTelefono() == null || usuario.getTelefono().trim().isEmpty()) {
            throw new IllegalArgumentException("El teléfono es requerido");
        }
        return usuarioRepository.save(usuario);
    }

    public Usuario actualizar(Long id, Usuario usuario) {
        Usuario existente = obtenerPorId(id);
        existente.setNombre(usuario.getNombre());
        existente.setEmail(usuario.getEmail());
        existente.setTelefono(usuario.getTelefono());
        existente.setObservacion(usuario.getObservacion());
        return usuarioRepository.save(existente);
    }

    public void eliminar(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado con id: " + id);
        }
        usuarioRepository.deleteById(id);
    }
}