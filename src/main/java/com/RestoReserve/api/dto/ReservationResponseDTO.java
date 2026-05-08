package com.RestoReserve.api.dto;

import com.RestoReserve.api.model.Reserva;

import java.time.LocalDateTime;

public record ReservationResponseDTO(
    Long id,
    LocalDateTime fechahora,
    int numeropersonas,
    String estado,
    String comentario,
    String mesaNumero,
    String clienteNombre
) {
    public static ReservationResponseDTO fromEntity(Reserva reserva) {
        String mesaNumero = null;
        if (reserva.getMesas() != null && !reserva.getMesas().isEmpty()) {
            mesaNumero = "Mesa " + reserva.getMesas().iterator().next().getNumeroDeMesa();
        }
        
        return new ReservationResponseDTO(
            reserva.getId(),
            reserva.getFechahora(),
            reserva.getNumeropersonas(),
            reserva.getEstado() != null ? reserva.getEstado().name() : null,
            reserva.getComentario(),
            mesaNumero,
            reserva.getUsuario() != null ? reserva.getUsuario().getNombre() : null
        );
    }
}