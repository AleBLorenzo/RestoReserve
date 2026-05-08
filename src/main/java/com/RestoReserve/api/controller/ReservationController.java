package com.RestoReserve.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.RestoReserve.api.dto.ReservationRequestDTO;
import com.RestoReserve.api.dto.ReservationResponseDTO;
import com.RestoReserve.api.model.EstadoReserva;
import com.RestoReserve.api.service.ReservationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @GetMapping
    public ResponseEntity<List<ReservationResponseDTO>> listar(
            @RequestParam(required = false) EstadoReserva estado,
            @RequestParam(required = false) Long usuarioId) {
        
        if (estado != null) {
            return ResponseEntity.ok(reservationService.listarPorEstado(estado));
        }
        if (usuarioId != null) {
            return ResponseEntity.ok(reservationService.listarPorUsuario(usuarioId));
        }
        return ResponseEntity.ok(reservationService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<ReservationResponseDTO> crear(
            @Valid @RequestBody ReservationRequestDTO dto,
            @RequestParam Long usuarioId) {
        ReservationResponseDTO response = reservationService.crear(dto, usuarioId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<ReservationResponseDTO> cambiarEstado(
            @PathVariable Long id,
            @RequestParam EstadoReserva estado) {
        return ResponseEntity.ok(reservationService.actualizarEstado(id, estado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        reservationService.cancelar(id);
        return ResponseEntity.noContent().build();
    }
}