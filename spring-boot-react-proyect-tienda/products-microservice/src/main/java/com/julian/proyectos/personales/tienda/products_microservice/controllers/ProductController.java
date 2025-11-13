package com.julian.proyectos.personales.tienda.products_microservice.controllers;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.julian.proyectos.personales.tienda.libs_commons.models.Product;
import com.julian.proyectos.personales.tienda.products_microservice.services.ProductService;

@RestController
public class ProductController {
    @Autowired
    private ProductService service;

    @GetMapping
    public ResponseEntity<List<Product>> findAllProducts() {
        return ResponseEntity.ok().body(service.findAllProducts());
    }

    @GetMapping("/{entry}")
    public ResponseEntity<?> findProductByIdOrName(@PathVariable String entry) {
        Optional<Product> product = service.findProductByIdOrName(entry);
        return product.isPresent() ? ResponseEntity.ok().body(product.get())
                : ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Collections.singletonMap("message", "El producto no existe!"));
    }

    @PostMapping
    public ResponseEntity<?> saveProduct(@RequestBody Product product) {
        return service.findProductByIdOrName(product.getName()).isEmpty()
                ? ResponseEntity.status(HttpStatus.CREATED).body(service.saveProduct(product))
                : ResponseEntity.status(HttpStatus.CONFLICT).body(Collections.singletonMap("message",
                        "El nombre '" + product.getName() + "' ya esta registrado, pruebe con otro nombre!"));
    }

    @PutMapping("/{entry}")
    public ResponseEntity<?> updateProductByIdOrName(@PathVariable String entry, @RequestBody Product product) {
        Optional<Product> productOpt = service.findProductByIdOrName(entry);
        if (productOpt.isPresent()) {
            return service.updateProductByIdOrName(entry, product).isPresent()
                    ? ResponseEntity.status(HttpStatus.CREATED).body(product)
                    : ResponseEntity.status(HttpStatus.CONFLICT).body(Collections.singletonMap("message",
                            "El nombre '" + product.getName() + "' ya esta registrado, pruebe con otro nombre!"));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap("message", "El producto no existe!"));
    }

    @DeleteMapping("/{entry}")
    public ResponseEntity<?> deleteProductByIdOrName(@PathVariable String entry) {
        if (service.findProductByIdOrName(entry).isPresent()) {
            service.deleteProductByIdOrName(entry);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message",
                "El producto no existe!"));
    }

}
