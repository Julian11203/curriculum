package com.julian.proyectos.personales.tienda.items_microservice.clients;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Value("${products-microservice.url}")
    private String url;

    @Bean
    @LoadBalanced
    WebClient.Builder webClient(){
        return WebClient.builder().baseUrl(url);
    };
}
