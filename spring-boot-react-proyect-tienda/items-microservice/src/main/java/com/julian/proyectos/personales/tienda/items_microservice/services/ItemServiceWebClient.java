package com.julian.proyectos.personales.tienda.items_microservice.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.julian.proyectos.personales.tienda.items_microservice.models.Item;
import com.julian.proyectos.personales.tienda.libs_commons.models.Product;

@Service
public class ItemServiceWebClient implements ItemService {

    @Autowired
    private WebClient.Builder client;

    @Override
    public List<Item> findAllProducts() {
        return client.build()
                .get()
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(Product.class)
                .map(product -> new Item(product, new Random().nextInt(10) + 1))
                .collectList()
                .block();
    }

    @Override
    public Optional<Item> findProductByIdOrName(String entry) {
        try {
            return client.build()
                    .get()
                    .uri("/{entry}", Map.of("entry", entry))
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(Product.class)
                    .map(product -> new Item(product, new Random().nextInt(10) + 1))
                    .blockOptional();
        } catch (WebClientResponseException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Item> saveProduct(Product product) {
        try {
            return client.build()
                    .post()
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(product)
                    .retrieve()
                    .bodyToMono(Product.class)
                    .map(p -> new Item(p, new Random().nextInt(10) + 1))
                    .blockOptional();
        } catch (WebClientResponseException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<?> updateProductByIdOrName(String entry, Product product) {
        try {
            return client.build()
                    .put()
                    .uri("/{entry}", Map.of("entry", entry))
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(product)
                    .retrieve()
                    .bodyToMono(Product.class)
                    .map(p -> new Item(p, new Random().nextInt(10) + 1))
                    .blockOptional();
        } catch (WebClientResponseException e) {
            return Optional.of(e);
        }
    }

    @Override
    public Optional<?> deleteProductByIdOrName(String entry) {
        try {
            return client.build()
                    .delete()
                    .uri("/{entry}", Map.of("entry", entry))
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .blockOptional();
        } catch (WebClientResponseException e) {
            return Optional.empty();
        }
    }

}
