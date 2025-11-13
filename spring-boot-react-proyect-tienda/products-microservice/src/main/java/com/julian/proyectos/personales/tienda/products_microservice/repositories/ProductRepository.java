package com.julian.proyectos.personales.tienda.products_microservice.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.julian.proyectos.personales.tienda.libs_commons.models.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{

    Optional<Product> findByName(String name);

    void deleteByName(String name);

}
