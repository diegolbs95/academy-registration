package com.gym.registration.service;

import com.gym.registration.config.RabbitMQConfig;
import com.gym.registration.dto.CadastrosDto;
import com.gym.registration.entity.Cadastros;
import com.gym.registration.enums.FormaPagamento;
import com.gym.registration.enums.Planos;
import com.gym.registration.exceptions.ErroCadastroClienteException;
import com.gym.registration.exceptions.ErrorBuscarClientesException;
import com.gym.registration.repository.CadastroRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.gym.registration.AppConstant.FILA_ENVIO_EMAIL;
import static com.gym.registration.AppConstant.FORMATO_DATA;

@Service
@Slf4j
public class CadastroService {

    @Autowired
    private CadastroRepository repository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public List<Cadastros> todosCadastros () {
        try {
            log.info("Buscando lista de cliente cadastrados.");
            return repository.findAll();

        } catch (Exception e) {
            throw new ErrorBuscarClientesException(String.format("Error buscar clientes causa: %s", e.getCause()));
        }
    }

    public Cadastros buscarAlunoCodigoEntrada (Integer codigoEntrada) {
        return repository.findByCodigoEntradaId(codigoEntrada)
                .orElseThrow(ErrorBuscarClientesException::new);
    }

    public String adicionarCliente (CadastrosDto dto) {
        try {
            dto.setDataRegistro(LocalDateTime.now().format(DateTimeFormatter.ofPattern(FORMATO_DATA)));

            var cliente = Cadastros.builder()
                    .endereco(dto.getEndereco())
                    .idade(dto.getIdade())
                    .email(dto.getEmail())
                    .nome(dto.getNome())
                    .numero(dto.getNumero())
                    .planos(dto.getPlanos())
                    .formaPagamento(dto.getFormaPagamento())
                    .codigoEntradaId(geradorProtocolo())
                    .dataRegistro(dto.getDataRegistro())
                    .dataFimVigencia(gerarDataFimVigencia(dto))
                    .build();

            repository.save(cliente);
            log.info("Cliente cadastrado com sucesso");

            var message = new Message(cliente.getEmail().getBytes());

            rabbitTemplate.send(FILA_ENVIO_EMAIL, message);

            return "CLIENTE_CADASTRADO";
        }catch (Exception e){
            throw new ErroCadastroClienteException("Erro ao adicionar cliente", e);
        }
    }

    public String liberarCatraca (Integer codigoEntrada) {

      var aluno = repository.findByCodigoEntradaId(codigoEntrada);
      if (aluno.isPresent()){
          log.info("Aluno encontrado!");
          return "Catraca liberada!";
      }
      throw new ErrorBuscarClientesException("Aluno não encontrado!");
    }

    public String efetuarPagamento (Integer codigoEntrada, Planos planoRenovacao, FormaPagamento formaPagamento) {

        var aluno = repository.findByCodigoEntradaId(codigoEntrada);

        if (aluno.isPresent()){
            var ultimoMes = aluno.get().getDataFimVigencia();
            var dataResponse = LocalDateTime.parse(ultimoMes, DateTimeFormatter.ofPattern(FORMATO_DATA));

            switch (planoRenovacao){
                case ANUAL -> aluno.get().setDataFimVigencia(dataResponse.plusYears(1).format(DateTimeFormatter.ofPattern(FORMATO_DATA)));
                case MENSAL -> aluno.get().setDataFimVigencia(dataResponse.plusMonths(1).format(DateTimeFormatter.ofPattern(FORMATO_DATA)));
                case SEMESTRAL -> aluno.get().setDataFimVigencia(dataResponse.plusMonths(6).format(DateTimeFormatter.ofPattern(FORMATO_DATA)));
                case TREMESTRAL -> aluno.get().setDataFimVigencia(dataResponse.plusMonths(3).format(DateTimeFormatter.ofPattern(FORMATO_DATA)));
            }

            if (aluno.get().getAlunoRenovado() == null){
                aluno.get().setAlunoRenovado(Boolean.TRUE);
            }

            if (!aluno.get().getFormaPagamento().equals(formaPagamento)){
                aluno.get().setFormaPagamento(formaPagamento);
            }

            repository.save(aluno.get());
            log.info("Aluno atualizado na base de dados!");
            return "PAGAMENTO_EFETUADO";
        }
        throw new ErrorBuscarClientesException("Aluno não encontrado!");
    }

    private Integer geradorProtocolo () {
        long dozeDigitos ;
        dozeDigitos = (long) (10000L + Math.random() * 89999L);
        return (int) dozeDigitos;
    }

    private String gerarDataFimVigencia (CadastrosDto aluno) {
        var dataRegistro = LocalDateTime.now().format(DateTimeFormatter.ofPattern(FORMATO_DATA));
        var response = LocalDateTime.parse(dataRegistro, DateTimeFormatter.ofPattern(FORMATO_DATA));

         switch (aluno.getPlanos()) {
            case ANUAL -> aluno.setDataFimVigencia(response.plusYears(1).format(DateTimeFormatter.ofPattern(FORMATO_DATA)));
            case MENSAL -> aluno.setDataFimVigencia(response.plusMonths(1).format(DateTimeFormatter.ofPattern(FORMATO_DATA)));
            case SEMESTRAL -> aluno.setDataFimVigencia(response.plusMonths(6).format(DateTimeFormatter.ofPattern(FORMATO_DATA)));
            case TREMESTRAL -> aluno.setDataFimVigencia(response.plusMonths(3).format(DateTimeFormatter.ofPattern(FORMATO_DATA)));
        }
        return aluno.getDataFimVigencia();
    }
}
