package com.RestoReserve.api.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "reserva")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false)
    private LocalDateTime horainicio;

    @Column(nullable = false)
    private LocalDateTime horafin;

    @Column(nullable = false)
    private int numeropersonas;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private estadoReserva estado = estadoReserva.PENDIENTE;

    @Column(length = 500)
    private String comentario;

    @ManyToMany
    @JoinTable(name = "reserva_mesa", joinColumns = @JoinColumn(name = "reserva_id"), inverseJoinColumns = @JoinColumn(name = "mesa_id"))
    private List<Mesa> mesas;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

}
