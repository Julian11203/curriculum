package com.julian.proyectos.personales.tienda.products_microservice.services;

import java.util.List;
import java.util.Optional;

import com.julian.proyectos.personales.tienda.libs_commons.models.Product;

public interface ProductService {
    List<Product> findAllProducts();

    Optional<Product> findProductByIdOrName(String entry);

    Optional<Product> saveProduct(Product product);

    Optional<Product> updateProductByIdOrName(String entry, Product product);

    void deleteProductByIdOrName(String entry);
}
