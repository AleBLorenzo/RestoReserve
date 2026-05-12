package com.RestoReserve.api.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.RestoReserve.api.model.Reserva;
import com.RestoReserve.api.model.EstadoReserva;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findByEstado(EstadoReserva estado);

    List<Reserva> findByFechahora(LocalDateTime fechahora);

    List<Reserva> findByUsuarioId(Long usuarioId);

    List<Reserva> findByUsuarioEmail(String email);

    // Buscar reservas activas de una mesa en un rango de 2 horas
    @Query("SELECT r FROM Reserva r JOIN r.mesas m WHERE r.estado <> 'CANCELADA' AND m.id = :mesaId AND r.fechahora BETWEEN :inicio AND :fin")
    List<Reserva> findReservasEnFranjaHoraria(
        @Param("mesaId") Long mesaId,
        @Param("inicio") LocalDateTime inicio,
        @Param("fin") LocalDateTime fin
    );
}