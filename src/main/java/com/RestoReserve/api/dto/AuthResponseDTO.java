package com.RestoReserve.api.dto;

import com.RestoReserve.api.model.TipoUsuario;

public record AuthResponseDTO(
    String token,
    String type,
    Long usuarioId,
    String nombre,
    TipoUsuario rol
) {
    public AuthResponseDTO(String token, Long usuarioId, String nombre, TipoUsuario rol) {
        this(token, "Bearer", usuarioId, nombre, rol);
    }
}