package com.RestoReserve.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record MesaRequestDTO(
    @NotNull(message = "El número de mesa es requerido")
    @Min(value = 1)
    int numeroDeMesa,
    
    @NotNull(message = "La capacidad es requerida")
    @Min(value = 1)
    int capacidad
) {}