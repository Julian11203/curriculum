package com.julian.proyectos.personales.tienda.items_microservice.services;

import java.util.List;
import java.util.Optional;

import com.julian.proyectos.personales.tienda.items_microservice.models.Item;
import com.julian.proyectos.personales.tienda.libs_commons.models.Product;

public interface ItemService {
    List<Item> findAllProducts();

    Optional<Item> findProductByIdOrName(String entry);

    Optional<Item> saveProduct(Product product);

    Optional<?> updateProductByIdOrName(String entry, Product product);

    Optional<?> deleteProductByIdOrName(String entry);
}
