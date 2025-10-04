package com.fiap.restapi;

import com.fiap.restapi.model.Professor;
import com.fiap.restapi.repository.ProfessorRepository;
import com.fiap.restapi.service.ProfessorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfessorServiceTest {

    @Mock
    private ProfessorRepository repo;

    @InjectMocks
    private ProfessorService service;

    private Professor sampleProfessor;

    @BeforeEach
    void setUp() {
        sampleProfessor = new Professor(1L, "Maria Santos", "TI", "maria@email.com", "Doutora");
    }

    @Test
    void adicionar_Success() {
        // Arrange
        when(repo.adicionar(any(Professor.class))).thenReturn(sampleProfessor);

        // Act
        Professor result = service.adicionar("Maria Santos", "TI", "maria@email.com", "Doutora");

        // Assert
        assertNotNull(result);
        assertEquals("Maria Santos", result.getNome());
        assertEquals("maria@email.com", result.getEmail());
        verify(repo, times(1)).adicionar(any(Professor.class));
    }

    @Test
    void adicionar_EmailInvalido_ThrowsException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.adicionar("Nome", "Dept", "email-invalido", "Tit"));
        assertEquals("Email inválido", exception.getMessage());
        verify(repo, never()).adicionar(any(Professor.class));
    }

    @Test
    void adicionar_DepartamentoExcedeLimite_ThrowsException() {
        // Act & Assert
        String longDept = "D".repeat(101);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.adicionar("Nome", longDept, "email@valid.com", "Tit"));
        assertEquals("Departamento excede 100 caracteres", exception.getMessage());
        verify(repo, never()).adicionar(any(Professor.class));
    }

    @Test
    void adicionar_TitulacaoNull_Success() {
        // Arrange
        Professor nullTitProfessor = new Professor(1L, "Nome", "Dept", "email@valid.com", null);
        when(repo.adicionar(any(Professor.class))).thenReturn(nullTitProfessor);

        // Act
        Professor result = service.adicionar("Nome", "Dept", "email@valid.com", null);

        // Assert
        assertNotNull(result);
        assertNull(result.getTitulacao());
        verify(repo, times(1)).adicionar(any(Professor.class));
    }

    @Test
    void adicionar_TitulacaoExcedeLimite_ThrowsException() {
        // Act & Assert
        String longTit = "T".repeat(81);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.adicionar("Nome", "Dept", "email@valid.com", longTit));
        assertEquals("Titulação excede 80 caracteres", exception.getMessage());
        verify(repo, never()).adicionar(any(Professor.class));
    }

    @Test
    void buscarPorId_Success() {
        // Arrange
        when(repo.buscarPorId(1L)).thenReturn(Optional.of(sampleProfessor));

        // Act
        Optional<Professor> result = service.buscarPorId(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(sampleProfessor, result.get());
        verify(repo, times(1)).buscarPorId(1L);
    }

    @Test
    void buscarPorId_InvalidoId_ThrowsException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.buscarPorId(0L));
        assertEquals("Id inválido", exception.getMessage());
        verify(repo, never()).buscarPorId(anyLong());
    }

    @Test
    void buscarPorId_NotFound() {
        // Arrange
        when(repo.buscarPorId(999L)).thenReturn(Optional.empty());

        // Act
        Optional<Professor> result = service.buscarPorId(999L);

        // Assert
        assertFalse(result.isPresent());
        verify(repo, times(1)).buscarPorId(999L);
    }

    @Test
    void listar_Success() {
        // Arrange
        List<Professor> expectedList = List.of(sampleProfessor);
        when(repo.listar()).thenReturn(expectedList);

        // Act
        List<Professor> result = service.listar();

        // Assert
        assertEquals(expectedList, result);
        verify(repo, times(1)).listar();
    }

    @Test
    void atualizar_Success() {
        // Arrange - Crie um objeto atualizado para simular o retorno do repo após update
        Professor updatedProfessor = new Professor(1L, "Maria Atualizado", "TI Novo", "maria2@email.com", "Mestre");
        when(repo.atualizar(eq(1L), any(Professor.class))).thenReturn(Optional.of(updatedProfessor));

        // Act
        Optional<Professor> result = service.atualizar(1L, "Maria Atualizado", "TI Novo", "maria2@email.com", "Mestre");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Maria Atualizado", result.get().getNome());
        assertEquals("maria2@email.com", result.get().getEmail());
        verify(repo, times(1)).atualizar(eq(1L), any(Professor.class));
    }

    @Test
    void atualizar_InvalidoId_ThrowsException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.atualizar(-1L, "Nome", "Dept", "email@valid.com", "Tit"));
        assertEquals("Id inválido", exception.getMessage());
        verify(repo, never()).atualizar(anyLong(), any(Professor.class));
    }

    @Test
    void atualizar_EmailInvalido_ThrowsException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.atualizar(1L, "Nome", "Dept", "invalido", "Tit"));
        assertEquals("Email inválido", exception.getMessage());
        verify(repo, never()).atualizar(anyLong(), any(Professor.class));
    }

    @Test
    void atualizar_NotFound() {
        // Arrange
        when(repo.atualizar(eq(999L), any(Professor.class))).thenReturn(Optional.empty());

        // Act
        Optional<Professor> result = service.atualizar(999L, "Nome", "Dept", "email@valid.com", "Tit");

        // Assert
        assertFalse(result.isPresent());
        verify(repo, times(1)).atualizar(eq(999L), any(Professor.class));
    }

    @Test
    void deletar_Success() {
        // Arrange
        when(repo.deletar(1L)).thenReturn(true);

        // Act
        boolean result = service.deletar(1L);

        // Assert
        assertTrue(result);
        verify(repo, times(1)).deletar(1L);
    }

    @Test
    void deletar_NotFound() {
        // Arrange
        when(repo.deletar(999L)).thenReturn(false);

        // Act
        boolean result = service.deletar(999L);

        // Assert
        assertFalse(result);
        verify(repo, times(1)).deletar(999L);
    }

    @Test
    void deletar_InvalidoId_ThrowsException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.deletar(null));
        assertEquals("Id inválido", exception.getMessage());
        verify(repo, never()).deletar(anyLong());
    }
}
