package com.syrtin.dataprocessor.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "app")
public class AppProps {

    private Rabbitmq rabbitmq;

    @Data
    public static class Rabbitmq {
        private String exchange;

        private String requestRoutingKey;

        private String responseRoutingKey;

        private String requestQueue;

        private String responseQueue;

        private Long sleepTimeout;

    }
}