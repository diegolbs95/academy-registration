package com.gym.registration.entity;

import com.gym.registration.enums.FormaPagamento;
import com.gym.registration.enums.Planos;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Cadastros {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private Integer idade;
    private String numero;
    private Integer codigoEntrada;

    private Planos planos;
    private FormaPagamento formaPagamento;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "endereco_id")
    private Endereco endereco;
}
