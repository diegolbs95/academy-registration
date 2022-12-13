package com.gym.registration.controller;

import com.gym.registration.dto.CadastrosDto;
import com.gym.registration.entity.Cadastros;
import com.gym.registration.enums.FormaPagamento;
import com.gym.registration.enums.Planos;
import com.gym.registration.exceptions.ErroCadastroClienteException;
import com.gym.registration.exceptions.ErrorBuscarClientesException;
import com.gym.registration.repository.CadastroRepository;
import com.gym.registration.service.CadastroService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cadastros")
@Slf4j
public class CadastroController {

    @Autowired
    private CadastroService cadastroService;
    @Autowired
    private CadastroRepository repository;

    @GetMapping("/{id}")
    public Cadastros buscarCadastrosId (@PathVariable Long id) {

        return repository.findById(id).orElseThrow();
    }

    @GetMapping("/buscar-aluno-codigo-entrada/{codigoEntrada}")
    public Cadastros buscarCadastroPorCodigoEntrada (@PathVariable Integer codigoEntrada) {
        try {
            log.info("Acionando API de buscar aluno por codigo de entrada!");
            return cadastroService.buscarAlunoCodigoEntrada(codigoEntrada);
        }catch (ErrorBuscarClientesException ex){
            log.info(String.format("Error ao buscar aluno por codigo de entrada. Tipo do error: %s", ex.getMessage()));
        }
        return cadastroService.buscarAlunoCodigoEntrada(codigoEntrada);
    }

    @PostMapping("/adicionar-aluno")
    public ResponseEntity<String> cadastrarCliente (@RequestBody CadastrosDto cadastros) {
        try {
            log.info("Acionando API adicionar cliente");
            var result = cadastroService.adicionarCliente(cadastros);

            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (ErroCadastroClienteException e){
            log.error(String.format("Erro ao adicionar cliente tipo do error: %s", e.getCause()));
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Error");
    }

    @GetMapping("/liberarCatraca")
    public ResponseEntity<String> liberarCatraca (@RequestHeader Integer codigoEntrada) {
        try {
            log.info("Acionando API liberar catraca!");
            return ResponseEntity.ok(cadastroService.liberarCatraca(codigoEntrada));
        }catch (ErrorBuscarClientesException ex){
            log.info("Aluno não encontrado!");
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Catraca não liberada");
        }
    }

    @PutMapping("/efetuar-pagamento")
    public ResponseEntity<String> efetuarPagamento (@RequestHeader Integer codigoEntrada,
                                                    @RequestHeader Planos planoRenovacao,
                                                    @RequestHeader FormaPagamento formaPagamento) {

        try {
            log.info("Acionando API de Efetuacao de Pagamento!");
            var response = cadastroService.efetuarPagamento(codigoEntrada, planoRenovacao, formaPagamento);
            return ResponseEntity.ok(response);
        }catch (ErrorBuscarClientesException ex){
            log.info(String.format("Error no fluxo de Efetuar Pagamento, tipo do error: %s", ex.getCause()));
            log.info(ex.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body("ALUNO_NAO_EXISTE");
    }
}
