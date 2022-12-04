package com.gym.registration.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static com.gym.registration.AppConstant.CLIENTE_NAO_ENCONTRATO;

@ResponseStatus(value = HttpStatus.NO_CONTENT)
public class ErrorBuscarClientesException extends RuntimeException{

    public ErrorBuscarClientesException(String e){
        super(CLIENTE_NAO_ENCONTRATO);
    }

    public ErrorBuscarClientesException(String mensagem, Throwable causa){
        super(mensagem, causa);
    }

    public ErrorBuscarClientesException() {
        super(CLIENTE_NAO_ENCONTRATO);
    }
}
