package com.RestoReserve.api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import java.util.List;

@Entity
@Table(name = "usuario")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false  )
    private String nombre;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false , unique = true)
    private int telefono;

    @Column
    private String observacion;

    @OneToMany
    private List<mesa> mesas;
        
    @OneToMany
    private List<reserva> reservas;



}
