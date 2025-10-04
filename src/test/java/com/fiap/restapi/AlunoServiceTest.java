package com.fiap.restapi;

import com.fiap.restapi.model.Aluno;
import com.fiap.restapi.repository.AlunoRepository;
import com.fiap.restapi.service.AlunoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlunoServiceTest {

    @Mock
    private AlunoRepository repo;

    @InjectMocks
    private AlunoService service;

    private Aluno sampleAluno;

    @BeforeEach
    void setUp() {
        sampleAluno = new Aluno(1L, "João Silva", "Engenharia de Software");
    }

    @Test
    void adicionar_Success() {
        // Arrange
        when(repo.adicionar(any(Aluno.class))).thenReturn(sampleAluno);

        // Act
        Aluno result = service.adicionar("João Silva", "Engenharia de Software");

        // Assert
        assertNotNull(result);
        assertEquals("João Silva", result.getNome());
        assertEquals("Engenharia de Software", result.getCurso());
        verify(repo, times(1)).adicionar(any(Aluno.class));
    }

    @Test
    void adicionar_NomeBlank_ThrowsException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.adicionar("", "Curso"));
        assertEquals("Nome é obrigatório", exception.getMessage());
        verify(repo, never()).adicionar(any(Aluno.class));
    }

    @Test
    void adicionar_CursoExcedeLimite_ThrowsException() {
        // Act & Assert
        String longCurso = "C".repeat(151);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.adicionar("Nome", longCurso));
        assertEquals("Curso excede 150 caracteres", exception.getMessage());
        verify(repo, never()).adicionar(any(Aluno.class));
    }

    @Test
    void buscarPorId_Success() {
        // Arrange
        when(repo.buscarPorId(1L)).thenReturn(Optional.of(sampleAluno));

        // Act
        Optional<Aluno> result = service.buscarPorId(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(sampleAluno, result.get());
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
        Optional<Aluno> result = service.buscarPorId(999L);

        // Assert
        assertFalse(result.isPresent());
        verify(repo, times(1)).buscarPorId(999L);
    }

    @Test
    void listar_Success() {
        // Arrange
        List<Aluno> expectedList = List.of(sampleAluno);
        when(repo.listar()).thenReturn(expectedList);

        // Act
        List<Aluno> result = service.listar();

        // Assert
        assertEquals(expectedList, result);
        verify(repo, times(1)).listar();
    }

    @Test
    void atualizar_Success() {
        // Arrange - Crie um objeto atualizado para simular o retorno do repo após update
        Aluno updatedAluno = new Aluno(1L, "João Silva Atualizado", "Novo Curso");
        when(repo.atualizar(eq(1L), any(Aluno.class))).thenReturn(Optional.of(updatedAluno));

        // Act
        Optional<Aluno> result = service.atualizar(1L, "João Silva Atualizado", "Novo Curso");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("João Silva Atualizado", result.get().getNome());
        assertEquals("Novo Curso", result.get().getCurso());
        verify(repo, times(1)).atualizar(eq(1L), any(Aluno.class));
    }

    @Test
    void atualizar_InvalidoId_ThrowsException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.atualizar(-1L, "Nome", "Curso"));
        assertEquals("Id inválido", exception.getMessage());
        verify(repo, never()).atualizar(anyLong(), any(Aluno.class));
    }

    @Test
    void atualizar_NomeExcedeLimite_ThrowsException() {
        // Act & Assert
        String longNome = "N".repeat(151);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.atualizar(1L, longNome, "Curso"));
        assertEquals("Nome excede 150 caracteres", exception.getMessage());
        verify(repo, never()).atualizar(anyLong(), any(Aluno.class));
    }

    @Test
    void atualizar_NotFound() {
        // Arrange
        when(repo.atualizar(eq(999L), any(Aluno.class))).thenReturn(Optional.empty());

        // Act
        Optional<Aluno> result = service.atualizar(999L, "Nome", "Curso");

        // Assert
        assertFalse(result.isPresent());
        verify(repo, times(1)).atualizar(eq(999L), any(Aluno.class));
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
