package com.RestoReserve.api.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.RestoReserve.api.dto.ReservaRequestDTO;
import com.RestoReserve.api.dto.ReservaResponseDTO;
import com.RestoReserve.api.exception.GlobalExceptionHandler.BadRequestException;
import com.RestoReserve.api.exception.GlobalExceptionHandler.ResourceNotFoundException;
import com.RestoReserve.api.model.EstadoReserva;
import com.RestoReserve.api.model.Mesa;
import com.RestoReserve.api.model.Reserva;
import com.RestoReserve.api.model.TipoUsuario;
import com.RestoReserve.api.model.Usuario;
import com.RestoReserve.api.repository.MesaRepository;
import com.RestoReserve.api.repository.ReservaRepository;
import com.RestoReserve.api.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class ReservaServiceTest {

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private MesaRepository mesaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private ReservaService reservaService;

    private Mesa mesa;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        // Mesa de prueba
        mesa = new Mesa();
        mesa.setId(1L);
        mesa.setNumeroDeMesa(1);
        mesa.setCapacidad(4);
        mesa.setEstado(com.RestoReserve.api.model.EstadoMesa.LIBRE);

        // Usuario de prueba
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Juan");
        usuario.setEmail("juan@test.com");
        usuario.setRol(TipoUsuario.USER);
    }

    @Test
    void crear_ConFechaEnElPasado_LanzaBadRequestException() {
        // Arrange
        ReservaRequestDTO dtoPasado = new ReservaRequestDTO(
            1L,
            LocalDateTime.now().minusDays(1),
            2,
            "Test"
        );

        // Act & Assert
        BadRequestException exception = assertThrows(
            BadRequestException.class,
            () -> reservaService.crear(dtoPasado, "juan@test.com")
        );

        assertEquals("No se pueden crear reservas en el pasado", exception.getMessage());

        // Verificar que no se intentó guardar
        verify(reservaRepository, never()).save(any(Reserva.class));
    }

    @Test
    void crear_ConFechaFutura_Success() {
        // Arrange
        ReservaRequestDTO dtoFuturo = new ReservaRequestDTO(
            1L,
            LocalDateTime.now().plusDays(1),
            2,
            "Test futuro"
        );

        when(mesaRepository.findById(1L)).thenReturn(Optional.of(mesa));
        when(usuarioRepository.findByEmail("juan@test.com")).thenReturn(Optional.of(usuario));
        when(reservaRepository.findReservasEnFranjaHoraria(anyLong(), any(), any()))
            .thenReturn(Collections.emptyList());
        when(reservaRepository.save(any(Reserva.class))).thenAnswer(invocation -> {
            Reserva r = invocation.getArgument(0);
            r.setId(1L);
            return r;
        });

        // Act
        ReservaResponseDTO result = reservaService.crear(dtoFuturo, "juan@test.com");

        // Assert
        assertNotNull(result);
        assertEquals(EstadoReserva.PENDIENTE.name(), result.estado());
        verify(reservaRepository).save(any(Reserva.class));
    }

    @Test
    void crear_ConMesaNoExistente_LanzaResourceNotFoundException() {
        // Arrange
        ReservaRequestDTO dto = new ReservaRequestDTO(
            999L,
            LocalDateTime.now().plusDays(1),
            2,
            "Test"
        );

        when(mesaRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
            ResourceNotFoundException.class,
            () -> reservaService.crear(dto, "juan@test.com")
        );
    }

    @Test
    void crear_ConNumeroPersonasExcedeCapacidad_LanzaBadRequestException() {
        // Arrange
        ReservaRequestDTO dtoExcede = new ReservaRequestDTO(
            1L,
            LocalDateTime.now().plusDays(1),
            10,
            "Test"
        );

        when(mesaRepository.findById(1L)).thenReturn(Optional.of(mesa));
        when(usuarioRepository.findByEmail("juan@test.com")).thenReturn(Optional.of(usuario));

        // Act & Assert
        BadRequestException exception = assertThrows(
            BadRequestException.class,
            () -> reservaService.crear(dtoExcede, "juan@test.com")
        );

        assertTrue(exception.getMessage().contains("capacidad"));
    }
}