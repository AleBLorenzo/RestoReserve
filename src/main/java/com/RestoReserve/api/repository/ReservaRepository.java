package com.RestoReserve.api.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.RestoReserve.api.model.EstadoReserva;
import com.RestoReserve.api.model.Reserva;
import com.RestoReserve.api.model.Usuario;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findByFechahora(LocalDateTime fechahora);

    List<Reserva> findByUsuario(Usuario usuario);

    List<Reserva> findByUsuarioEmail(String email);
    
    List<Reserva> findByEstado(EstadoReserva estado);
}