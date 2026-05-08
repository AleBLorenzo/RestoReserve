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

import com.RestoReserve.api.dto.ReservaRequestDTO;
import com.RestoReserve.api.dto.ReservaResponseDTO;
import com.RestoReserve.api.model.EstadoReserva;
import com.RestoReserve.api.service.ReservaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/Reservas")
public class ReservaController {

    @Autowired
    private ReservaService ReservaService;

    @GetMapping
    public ResponseEntity<List<ReservaResponseDTO>> listar(
            @RequestParam(required = false) EstadoReserva estado,
            @RequestParam(required = false) Long usuarioId) {

        if (estado != null) {
            return ResponseEntity.ok(ReservaService.listarPorEstado(estado));
        }
        if (usuarioId != null) {
            return ResponseEntity.ok(ReservaService.listarPorUsuario(usuarioId));
        }
        return ResponseEntity.ok(ReservaService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservaResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ReservaService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<ReservaResponseDTO> crear(
            @Valid @RequestBody ReservaRequestDTO dto,
            @RequestParam Long usuarioId) {
        ReservaResponseDTO response = ReservaService.crear(dto, usuarioId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<ReservaResponseDTO> cambiarEstado(
            @PathVariable Long id,
            @RequestParam EstadoReserva estado) {
        return ResponseEntity.ok(ReservaService.actualizarEstado(id, estado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        ReservaService.cancelar(id);
        return ResponseEntity.noContent().build();
    }
}