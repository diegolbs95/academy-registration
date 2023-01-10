package com.gym.registration;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AppConstant {

    public static final String CLIENTE_NAO_ENCONTRATO = "Cliente não encontrato";
    public static final String ERROR_AO_ADICIONAR_CLIENTE = "Error ao adicionar Cliente!";

    public static final String FORMATO_DATA = "dd/MM/yyyy HH:mm:ss";

    //Nome da fila
    public static final String FILA_ENVIO_EMAIL = "orders.v1.order-created";
}
