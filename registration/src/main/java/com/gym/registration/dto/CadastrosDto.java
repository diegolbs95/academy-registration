package com.gym.registration.dto;

import com.gym.registration.entity.Endereco;
import com.gym.registration.enums.FormaPagamento;
import com.gym.registration.enums.Planos;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CadastrosDto {

    private Long id;

    private String nome;
    private Integer idade;
    private String numero;
    private Integer codigoEntrada;

    private Planos planos;
    private FormaPagamento formaPagamento;

    private Endereco endereco;
}
