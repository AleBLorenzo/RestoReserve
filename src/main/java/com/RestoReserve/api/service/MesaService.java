package com.RestoReserve.api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.RestoReserve.api.dto.MesaRequestDTO;
import com.RestoReserve.api.dto.MesaResponseDTO;
import com.RestoReserve.api.exception.GlobalExceptionHandler.BadRequestException;
import com.RestoReserve.api.exception.GlobalExceptionHandler.ResourceNotFoundException;
import com.RestoReserve.api.model.EstadoMesa;
import com.RestoReserve.api.model.Mesa;
import com.RestoReserve.api.repository.MesaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MesaService {

    private final MesaRepository mesaRepository;

    public List<MesaResponseDTO> listar() {
        return mesaRepository.findAll().stream()
                .map(MesaResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<MesaResponseDTO> listarporCantidadPersonas(int cantidad) {
        
        List<Mesa> mesas = mesaRepository.findAll();
      
        List<Mesa> mesasDisponibles = new ArrayList<>();

        for (Mesa elem : mesas) {
            if (elem.getCapacidad() >= cantidad && elem.getEstado().equals(EstadoMesa.LIBRE)) {
                mesasDisponibles.add(elem);
            }
        }

        return mesasDisponibles.stream()
                .map(MesaResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public MesaResponseDTO obtenerPorId(Long id) {
        Mesa mesa = mesaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mesa no encontrada con id: " + id));
        return MesaResponseDTO.fromEntity(mesa);
    }

    public List<MesaResponseDTO> obtenerVip() {
        return mesaRepository.findByVip(true).stream()
                .map(MesaResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public MesaResponseDTO guardar(MesaRequestDTO dto) {
        if (dto.capacidad() <= 0) {
            throw new BadRequestException("La capacidad debe ser mayor a 0");
        }
        Mesa mesa = new Mesa();
        mesa.setNumeroDeMesa(dto.numeroDeMesa());
        mesa.setCapacidad(dto.capacidad());
        mesa.setEstado(EstadoMesa.LIBRE);
        mesa = mesaRepository.save(mesa);
        return MesaResponseDTO.fromEntity(mesa);
    }

    public MesaResponseDTO actualizar(Long id, MesaRequestDTO dto) {
        Mesa existente = mesaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mesa no encontrada con id: " + id));
        existente.setNumeroDeMesa(dto.numeroDeMesa());
        existente.setCapacidad(dto.capacidad());
        existente = mesaRepository.save(existente);
        return MesaResponseDTO.fromEntity(existente);
    }

    public MesaResponseDTO cambiarEstado(Long id, EstadoMesa estado) {
        Mesa mesa = mesaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mesa no encontrada"));
        mesa.setEstado(estado);
        mesa = mesaRepository.save(mesa);
        return MesaResponseDTO.fromEntity(mesa);
    }

    public void eliminar(Long id) {
        if (!mesaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Mesa no encontrada con id: " + id);
        }
        mesaRepository.deleteById(id);
    }
}