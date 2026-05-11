package com.RestoReserve.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.RestoReserve.api.config.JwtService;
import com.RestoReserve.api.dto.AuthRequestDTO;
import com.RestoReserve.api.dto.AuthResponseDTO;
import com.RestoReserve.api.dto.RegisterRequestDTO;
import com.RestoReserve.api.exception.GlobalExceptionHandler.BadRequestException;
import com.RestoReserve.api.exception.GlobalExceptionHandler.UnauthorizedException;
import com.RestoReserve.api.exception.GlobalExceptionHandler.ConflictException;
import com.RestoReserve.api.model.Usuario;
import com.RestoReserve.api.model.TipoUsuario;
import com.RestoReserve.api.repository.UsuarioRepository;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    public AuthResponseDTO login(AuthRequestDTO request) {
        Usuario usuario = usuarioRepository.findByEmail(request.username())
            .orElseThrow(() -> new UnauthorizedException("Credenciales inválidas"));

        if (!passwordEncoder.matches(request.password(), usuario.getContrasena())) {
            throw new UnauthorizedException("Credenciales inválidas");
        }

        String token = jwtService.generateToken(usuario.getEmail(), usuario.getRol());
        return new AuthResponseDTO(token, usuario.getId(), usuario.getNombre(), usuario.getRol());
    }

    public AuthResponseDTO register(RegisterRequestDTO request) {
        if (usuarioRepository.findByEmail(request.email()).isPresent()) {
            throw new ConflictException("El email ya está registrado");
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(request.nombre());
        usuario.setEmail(request.email());
        usuario.setTelefono(request.telefono());
        usuario.setContrasena(passwordEncoder.encode(request.password()));
        usuario.setRol(TipoUsuario.USER);
        
        usuario = usuarioRepository.save(usuario);

        String token = jwtService.generateToken(usuario.getEmail(), usuario.getRol());
        return new AuthResponseDTO(token, usuario.getId(), usuario.getNombre(), usuario.getRol());
    }
}