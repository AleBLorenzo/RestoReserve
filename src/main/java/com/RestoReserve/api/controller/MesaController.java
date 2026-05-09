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

import com.RestoReserve.api.dto.MesaRequestDTO;
import com.RestoReserve.api.dto.MesaResponseDTO;
import com.RestoReserve.api.model.EstadoMesa;
import com.RestoReserve.api.service.MesaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/tables")
public class MesaController {

    @Autowired
    private MesaService mesaService;

    @GetMapping
    public ResponseEntity<List<MesaResponseDTO>> listarMesas() {
        return ResponseEntity.ok(mesaService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MesaResponseDTO> obtenerMesa(@PathVariable Long id) {
        return ResponseEntity.ok(mesaService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<MesaResponseDTO> crearMesa(@Valid @RequestBody MesaRequestDTO dto) {
        return new ResponseEntity<>(mesaService.guardar(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MesaResponseDTO> actualizarMesa(@PathVariable Long id, @Valid @RequestBody MesaRequestDTO dto) {
        return ResponseEntity.ok(mesaService.actualizar(id, dto));
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<MesaResponseDTO> cambiarEstadoMesa(@PathVariable Long id, @RequestParam EstadoMesa estado) {
        return ResponseEntity.ok(mesaService.cambiarEstado(id, estado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarMesa(@PathVariable Long id) {
        mesaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}