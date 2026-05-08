package com.RestoReserve.api.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.RestoReserve.api.model.Reserva;
import com.RestoReserve.api.model.Usuario;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    Optional<Reserva> findByfecha(LocalDate fecha);

    Optional<Reserva> findByUsuario(Usuario usuario);
}