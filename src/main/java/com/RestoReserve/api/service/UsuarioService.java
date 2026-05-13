package com.RestoReserve.api.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.RestoReserve.api.dto.UsuarioRequestDTO;
import com.RestoReserve.api.dto.UsuarioResponseDTO;
import com.RestoReserve.api.exception.GlobalExceptionHandler.BadRequestException;
import com.RestoReserve.api.exception.GlobalExceptionHandler.ResourceNotFoundException;
import com.RestoReserve.api.model.EstadoPenalizacion;
import com.RestoReserve.api.model.TipoUsuario;
import com.RestoReserve.api.model.Usuario;
import com.RestoReserve.api.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public List<UsuarioResponseDTO> listarDTO() {
        return usuarioRepository.findAll().stream()
            .map(UsuarioResponseDTO::fromEntity)
            .collect(Collectors.toList());
    }

    public UsuarioResponseDTO obtenerPorEmain(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con email: " + email));
        return UsuarioResponseDTO.fromEntity(usuario);
    }
    public UsuarioResponseDTO guardar(UsuarioRequestDTO dto) {
        // Validar email único
        if (usuarioRepository.findByEmail(dto.email()).isPresent()) {
            throw new BadRequestException("El email ya está registrado");
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(dto.nombre());
        usuario.setEmail(dto.email());
        usuario.setTelefono(dto.telefono());
        usuario.setContrasena(dto.contrasena());  
        usuario.setObservacion(dto.observacion());
        usuario.setPenalizationPoints(0);
        usuario.setPenalizacion(EstadoPenalizacion.ACTIVE);
        usuario.setRol(TipoUsuario.USER);

        usuario = usuarioRepository.save(usuario);
        return UsuarioResponseDTO.fromEntity(usuario);
    }

    public UsuarioResponseDTO resetearPenalizacion(Long id, EstadoPenalizacion estado) {
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));

        usuario.setPenalizacion(estado != null ? estado : EstadoPenalizacion.ACTIVE);

        usuario = usuarioRepository.save(usuario);
        return UsuarioResponseDTO.fromEntity(usuario);
    }

    public void eliminar(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuario no encontrado con id: " + id);
        }
        usuarioRepository.deleteById(id);
    }
}