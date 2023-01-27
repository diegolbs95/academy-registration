package com.gym.registration.factory;

import com.gym.registration.dto.CadastrosDto;
import com.gym.registration.entity.Cadastros;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.gym.registration.AppConstant.FORMATO_DATA;

@UtilityClass
public class ClienteFactory {

    public static Cadastros criarCadastro (CadastrosDto dto) {
        return Cadastros.builder()
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
    }

    private static Integer geradorProtocolo () {
        long dozeDigitos ;
        dozeDigitos = (long) (10000L + Math.random() * 89999L);
        return (int) dozeDigitos;
    }

    private static String gerarDataFimVigencia (CadastrosDto aluno) {
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
