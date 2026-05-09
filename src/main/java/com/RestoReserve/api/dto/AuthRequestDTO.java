package com.RestoReserve.api.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthRequestDTO(
    @NotBlank(message = "El username es requerido")
    String username,
    
    @NotBlank(message = "La contraseña es requerida")
    String password
) {}