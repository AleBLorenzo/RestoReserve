package com.RestoReserve.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequestDTO(
    @NotBlank(message = "El nombre es requerido")
    @Size(min = 2, max = 100)
    String nombre,
    
    @NotBlank(message = "El email es requerido")
    @Email(message = "Email inválido")
    String email,
    
    @NotBlank(message = "El teléfono es requerido")
    @Size(min = 9, max = 15)
    String telefono,
    
    @NotBlank(message = "La contraseña es requerida")
    @Size(min = 6, message = "Mínimo 6 caracteres")
    String password
) {}