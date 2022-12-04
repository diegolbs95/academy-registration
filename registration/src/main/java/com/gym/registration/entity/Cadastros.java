package com.gym.registration.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gym.registration.enums.FormaPagamento;
import com.gym.registration.enums.Planos;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_cadastros")
public class Cadastros {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;
    @Column(nullable = false)
    private Integer idade;
    @Column(nullable = false)
    private String numero;

    private Integer codigoEntradaId;
    private Boolean alunoRenovado;

    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private Planos planos;
    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private FormaPagamento formaPagamento;

    @Nullable
    private String dataFimVigencia;
    @Nullable
    private String dataRegistro;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "endereco_id")
    private Endereco endereco;
}
