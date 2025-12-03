package com.julian.proyectos.personales.tienda.products_microservice.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.julian.proyectos.personales.tienda.libs_commons.models.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{

	@Query("""
			SELECT p
			FROM Product p
			WHERE CAST(p.id AS string) = :value
			   OR p.name = :value
			""")
	Optional<Product> findProductByIdOrName(String value);

}
