package com.julian.proyectos.personales.tienda.items_microservice.controllers;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.julian.proyectos.personales.tienda.items_microservice.models.Item;
import com.julian.proyectos.personales.tienda.items_microservice.services.ItemService;
import com.julian.proyectos.personales.tienda.libs_commons.models.Product;

@RestController
public class ItemController {
    @Autowired
    private ItemService service;

    @GetMapping
    public ResponseEntity<List<Item>> findAll() {
        return ResponseEntity.ok().body(service.findAllProducts());
    }

    @GetMapping("/{entry}")
    public ResponseEntity<?> findProductByIdOrName(@PathVariable String entry) {
        return service.findProductByIdOrName(entry).isPresent()
                ? ResponseEntity.ok().body(service.findProductByIdOrName(entry).get())
                : ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Collections.singletonMap("message", "El producto no existe!"));
    }

    @PostMapping
    public ResponseEntity<?> saveProduct(@RequestBody Product product) {
        Optional<Item> itemOptional = service.saveProduct(product);
        return itemOptional.isPresent() ? ResponseEntity.status(HttpStatus.CREATED).body(itemOptional.get())
                : ResponseEntity.status(HttpStatus.CONFLICT).body(Collections.singletonMap("message",
                        "El producto con nombre '" + product.getName() + "' ya existe!"));
    }

    @PutMapping("/{entry}")
    public ResponseEntity<?> updateProductByIdOrName(@PathVariable String entry, @RequestBody Product product) {
        Optional<?> resultado = service.updateProductByIdOrName(entry, product);
        if (resultado.get() instanceof WebClientResponseException responseEx) {
            if (responseEx.getStatusCode().equals(HttpStatusCode.valueOf(409))) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Collections.singletonMap("message",
                                "El nombre '" + product.getName() + "' ya esta registrado, pruebe con otro nombre!"));
            } else if (responseEx.getStatusCode().equals(HttpStatusCode.valueOf(404))) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Collections.singletonMap("message", "El producto no existe!"));
            }
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(resultado.get());
    }

    @DeleteMapping("/{entry}")
    public ResponseEntity<?> deleteProductByIdOrName(@PathVariable String entry){
        if (service.findProductByIdOrName(entry).isPresent()) {
            service.deleteProductByIdOrName(entry);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message", "El producto no existe!"));
    }

}
