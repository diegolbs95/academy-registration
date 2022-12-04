package com.gym.registration.exceptions;

public class ErroCadastroClienteException extends RuntimeException{

    public ErroCadastroClienteException (String e){
        super(e);
    }

    public ErroCadastroClienteException (String mensagem, Throwable causa){
        super(mensagem, causa);
    }
}
