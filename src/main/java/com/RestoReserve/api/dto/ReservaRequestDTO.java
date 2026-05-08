package com.RestoReserve.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ReservaRequestDTO(
        @NotNull(message = "La mesa es obligatoria") Long tableId,

        @NotNull(message = "La fecha y hora es obligatoria") LocalDateTime fechahora,

        @Min(value = 1, message = "Mínimo 1 comensal") int numeropersonas,

        String comentario) {
}