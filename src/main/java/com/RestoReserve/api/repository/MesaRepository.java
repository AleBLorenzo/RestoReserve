package com.RestoReserve.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.RestoReserve.api.model.EstadoMesa;
import com.RestoReserve.api.model.Mesa;


@Repository
public interface MesaRepository extends JpaRepository<Mesa, Long> {

    Optional<Mesa> findByNumeroDeMesa(int numeroDeMesa);

    List<Mesa> findByVip(boolean vip);

    boolean findByEstado(EstadoMesa estado);

}
