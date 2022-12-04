package com.gym.registration.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "tb_endereco")
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "Estado", nullable = false)
    private String estado;
    @Column(name = "Cidade", nullable = false)
    private String cidade;
    @Column(name = "Rua", nullable = false)
    private String rua;
    @Column(name = "Numero", nullable = false)
    private Integer numero;

    @OneToOne(mappedBy = "endereco")
    @JsonIgnore
    private Cadastros cadastros;
}
