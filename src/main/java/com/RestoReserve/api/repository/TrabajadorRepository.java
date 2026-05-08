package com.RestoReserve.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.RestoReserve.api.model.Trabajador;
import com.RestoReserve.api.model.tipoEmpelado;

@Repository
public interface TrabajadorRepository extends JpaRepository<Trabajador, Long> {

    Optional<Trabajador> findByNombreusuario(String nombreusuario);

    Optional<Trabajador> findByTipo(tipoEmpelado tipo);
}