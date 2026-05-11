package com.RestoReserve.api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.RestoReserve.api.exception.GlobalExceptionHandler.BadRequestException;
import com.RestoReserve.api.exception.GlobalExceptionHandler.ResourceNotFoundException;
import com.RestoReserve.api.model.Usuario;
import com.RestoReserve.api.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    public Usuario obtenerPorId(Long id) {
        return usuarioRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));
    }

    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con email: " + email));
    }

    public Usuario guardar(Usuario usuario) {
        if (usuario.getNombre() == null || usuario.getNombre().trim().isEmpty()) {
            throw new BadRequestException("El nombre es requerido");
        }
        if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
            throw new BadRequestException("El email es requerido");
        }
        if (usuario.getTelefono() == null || usuario.getTelefono().trim().isEmpty()) {
            throw new BadRequestException("El teléfono es requerido");
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
            throw new ResourceNotFoundException("Usuario no encontrado con id: " + id);
        }
        usuarioRepository.deleteById(id);
    }
}