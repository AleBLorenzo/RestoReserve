package com.RestoReserve.api.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.RestoReserve.api.model.Reserva;
import com.RestoReserve.api.model.EstadoReserva;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findByEstado(EstadoReserva estado);

    List<Reserva> findByFechahora(LocalDateTime fechahora);

    List<Reserva> findByUsuarioId(Long usuarioId);

    List<Reserva> findByUsuarioEmail(String email);

}