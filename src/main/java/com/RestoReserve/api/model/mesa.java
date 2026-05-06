package com.RestoReserve.api.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mesa")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class mesa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int numeroDeMesa;

    @Column(nullable = false)
    private int capacidad;

    @Column(nullable = false)
    private estadoMesa estado = estadoMesa.LIBRE;

    @ManyToMany
    private List<reserva> reservas;
}
