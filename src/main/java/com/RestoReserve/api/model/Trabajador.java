package com.RestoReserve.api.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "trabajador")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Trabajador {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    @Column(nullable = false)
    private String nombre ;

    @Column(nullable = false)
    private tipoEmpelado tipo;

     @Column(nullable = false)
    private String user;

     @Column(nullable = false)
    private String contrasena;

    @OneToMany
    private List<mesa> mesas;


}
