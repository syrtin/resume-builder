package com.syrtin.fileprinter.config;

import lombok.AllArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
@AllArgsConstructor
public class RabbitMQConfig {

    private final AppProps appProps;

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Queue requestQueue() {
        return new Queue(appProps.getRabbitmq().getRequestQueue(), true);
    }

    @Bean
    public Queue responseQueue() {
        return new Queue(appProps.getRabbitmq().getResponseQueue(), true);
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(appProps.getRabbitmq().getExchange());
    }

    @Bean
    public Binding bindingRequestQueue() {
        return BindingBuilder.bind(requestQueue()).to(exchange()).with(appProps.getRabbitmq().getRequestRoutingKey());
    }

    @Bean
    public Binding bindingResponseQueue() {
        return BindingBuilder.bind(responseQueue()).to(exchange()).with(appProps.getRabbitmq().getResponseRoutingKey());
    }

    @Bean
    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}

