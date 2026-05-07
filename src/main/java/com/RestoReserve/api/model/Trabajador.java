package com.RestoReserve.api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "trabajador")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Trabajador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false)
    private tipoEmpelado tipo;

    @Column(nullable = false, length = 30)
    private String nombreusuario;

    @Column(name = "login_user", nullable = false)
    private String contrasena;

}
