package com.RestoReserve.api.dto;

import com.RestoReserve.api.model.Mesa;

public record MesaResponseDTO(
    Long id,
    int numeroDeMesa,
    int capacidad,
    String estado
) {
    public static MesaResponseDTO fromEntity(Mesa mesa) {
        return new MesaResponseDTO(
            mesa.getId(),
            mesa.getNumeroDeMesa(),
            mesa.getCapacidad(),
            mesa.getEstado() != null ? mesa.getEstado().name() : null
        );
    }
}