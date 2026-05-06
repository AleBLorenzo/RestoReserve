package com.RestoReserve.api.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
@Table(name = "reserva")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false )
    private LocalDate fecha;

    @Column(nullable = false )
    private LocalDateTime horainicio;

    @Column(nullable = false )
    private LocalDateTime horafin;

    @Column(nullable = false )
    private int numeropersonas;

    @Column(nullable = false )
    private estadoReserva estado = estadoReserva.PENDIENTE;

    @Column
    private String comentario;

    @ManyToMany
    private List<mesa> mesas;



}
