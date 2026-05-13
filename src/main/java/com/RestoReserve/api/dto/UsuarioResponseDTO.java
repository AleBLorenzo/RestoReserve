package com.RestoReserve.api.dto;

import com.RestoReserve.api.model.Usuario;

public record UsuarioResponseDTO(
    Long id,
    String nombre,
    String email,
    String telefono,
    String observacion,
    String rol,
    int penalizationPoints,
    String penalizacion
) {
    public static UsuarioResponseDTO fromEntity(Usuario usuario) {
        return new UsuarioResponseDTO(
            usuario.getId(),
            usuario.getNombre(),
            usuario.getEmail(),
            usuario.getTelefono(),
            usuario.getObservacion(),
            usuario.getRol() != null ? usuario.getRol().name() : null,
            usuario.getPenalizationPoints(),
            usuario.getPenalizacion() != null ? usuario.getPenalizacion().name() : null
        );
    }
}