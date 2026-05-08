package com.RestoReserve.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.RestoReserve.api.model.TipoEmpelado;
import com.RestoReserve.api.model.Trabajador;

@Repository
public interface TrabajadorRepository extends JpaRepository<Trabajador, Long> {

    Optional<Trabajador> findByNombreusuario(String nombreusuario);

    Optional<Trabajador> findByTipo(TipoEmpelado tipo);
}