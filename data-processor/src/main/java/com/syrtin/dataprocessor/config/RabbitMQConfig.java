package com.syrtin.dataprocessor.config;

import lombok.AllArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.ClassMapper;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableRabbit
@AllArgsConstructor
public class RabbitMQConfig {
    private final static String FILE_PRINTER_RESPONSE_CLASS = "com.syrtin.fileprinter.dto.ResumePrintResponse";

    private final AppProps appProps;

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        converter.setClassMapper(customClassMapper());
        return converter;
    }

    @Bean
    public ClassMapper customClassMapper() {
        DefaultClassMapper classMapper = new DefaultClassMapper();
        Map<String, Class<?>> idClassMapping = new HashMap<>();
        idClassMapping.put(FILE_PRINTER_RESPONSE_CLASS, com.syrtin.dataprocessor.dto.ResumePrintResponse.class);
        classMapper.setIdClassMapping(idClassMapping);
        return classMapper;
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
    public Binding bindingRequest() {
        return BindingBuilder
                .bind(requestQueue())
                .to(exchange())
                .with(appProps.getRabbitmq().getRequestRoutingKey());
    }

    @Bean
    public Binding bindingResponse() {
        return BindingBuilder
                .bind(responseQueue())
                .to(exchange())
                .with(appProps.getRabbitmq().getResponseRoutingKey());
    }

    @Bean
    public SimpleMessageListenerContainer replyContainer(ConnectionFactory connectionFactory,
                                                         RabbitTemplate rabbitTemplate) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueues(responseQueue());
        container.setMessageListener(rabbitTemplate);
        return container;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        rabbitTemplate.setExchange(appProps.getRabbitmq().getExchange());
        rabbitTemplate.setRoutingKey(appProps.getRabbitmq().getRequestRoutingKey());
        rabbitTemplate.setReplyAddress(appProps.getRabbitmq().getResponseQueue());
        rabbitTemplate.setReplyTimeout(appProps.getRabbitmq().getSleepTimeout());
        return rabbitTemplate;
    }
}

