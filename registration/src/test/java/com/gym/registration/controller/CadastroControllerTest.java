package com.gym.registration.controller;

import com.gym.registration.dto.CadastrosDto;
import com.gym.registration.entity.Cadastros;
import com.gym.registration.enums.FormaPagamento;
import com.gym.registration.enums.Planos;
import com.gym.registration.exceptions.ErroCadastroClienteException;
import com.gym.registration.exceptions.ErrorBuscarClientesException;
import com.gym.registration.repository.CadastroRepository;
import com.gym.registration.service.CadastroService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CadastroControllerTest {

    @Mock
    CadastroService cadastroService;
    @Mock
    CadastroRepository repository;

    @InjectMocks
    CadastroController controller;

    Cadastros cadastros;
    CadastrosDto cadastrosDto;
    Planos planos;
    FormaPagamento formaPagamento;

    @BeforeEach
    void setUp() {

        cadastros = Cadastros.builder()
                .codigoEntradaId(32156)
                .id(1L)
                .nome("Galetinho")
                .build();

        planos = Planos.ANUAL;
        formaPagamento = FormaPagamento.CREDITO;
    }

    @Test
    void buscarCadastrosId() {

        when(repository.findById(anyLong())).thenReturn(Optional.ofNullable(cadastros));

        var result = controller.buscarCadastrosId(1L);

        assertEquals(cadastros, result);

    }

    @Test
    void buscarCadastroPorCodigoEntrada() {

        when(cadastroService.buscarAlunoCodigoEntrada(anyInt())).thenReturn(cadastros);
        var result = controller.buscarCadastroPorCodigoEntrada(32156);

        assertEquals(cadastros.getCodigoEntradaId(), result.getCodigoEntradaId());
    }

    @Test
    void buscarCadastroPorCodigoEntradaException() {

        doThrow(ErrorBuscarClientesException.class).when(cadastroService).buscarAlunoCodigoEntrada(anyInt());
        var result = Assertions.assertThrows(ErrorBuscarClientesException.class,
                () -> controller.buscarCadastroPorCodigoEntrada(98745)
                );
        assertNull(result.getMessage());
    }

    @Test
    void cadastrarCliente() {

        when(cadastroService.adicionarCliente(any())).thenReturn("CLIENTE_CADASTRADO");

        var result = controller.cadastrarCliente(cadastrosDto);

        assertEquals("CLIENTE_CADASTRADO", result.getBody());
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
    }

    @Test
    void cadastrarClienteException() {

        doThrow(ErroCadastroClienteException.class).when(cadastroService).adicionarCliente(any());

        var result = controller.cadastrarCliente(cadastrosDto);

        assertEquals("Error", result.getBody());
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
    }

    @Test
    void liberarCatraca() {

        when(cadastroService.liberarCatraca(anyInt())).thenReturn("Catraca liberada!");

        var result = controller.liberarCatraca(78954);

        assertEquals("Catraca liberada!", result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void liberarCatracaException() {

        doThrow(ErrorBuscarClientesException.class).when(cadastroService).liberarCatraca(anyInt());

        var result = controller.liberarCatraca(78954);

        assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
        assertEquals("Catraca não liberada", result.getBody());
    }

    @Test
    void efetuarPagamento() {

        when(cadastroService.efetuarPagamento(anyInt(), any(), any()))
                .thenReturn("PAGAMENTO_EFETUADO");

        var result = controller.efetuarPagamento(1, planos, formaPagamento);

        assertEquals("PAGAMENTO_EFETUADO", result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void efetuarPagamentoException () {

        doThrow(ErrorBuscarClientesException.class).when(cadastroService)
                .efetuarPagamento(anyInt(), any(), any());

        var result = controller.efetuarPagamento(1, planos, formaPagamento);

        assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
        assertEquals("ALUNO_NAO_EXISTE", result.getBody());
    }
}