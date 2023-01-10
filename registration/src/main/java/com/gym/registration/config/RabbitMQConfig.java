package com.gym.registration.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    //Criar nome para fila
    @Bean
    public Queue queue() {
        return new Queue("orders.v1.order-created");
    }

    //Bean para aplicacao criar a fila assim que seja iniciada
    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory factory) {
        return new RabbitAdmin(factory);
    }

    //Metodo esperado pelo spring assim que aplicacao se inicia
    //Chama o Bean do metodo anterior para ser inicializado
    @Bean
    public ApplicationListener<ApplicationReadyEvent> applicationReadyEventApplicationListener
                (RabbitAdmin rabbitAdmin) {
        return event -> rabbitAdmin.initialize();
    }
}
