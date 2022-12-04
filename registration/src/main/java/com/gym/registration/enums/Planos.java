package com.gym.registration.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Planos {

    ANUAL("ANUAL", 0),
    SEMESTRAL("SEMESTRAL", 1),
    TREMESTRAL("TRIMESTRAL", 2),
    MENSAL("MENSAL", 3);

    private final String status;
    private final Integer codigo;
}
