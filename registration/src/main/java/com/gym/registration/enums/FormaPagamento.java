package com.gym.registration.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FormaPagamento {

    CREDITO("CREDITO", 0),
    DEBITO("DEBITO", 1),
    PIX("PIX", 2),
    DINHEIRO("DINHEIRO", 3);

    private final String status;

    private final Integer codigo;
}
