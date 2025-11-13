package com.julian.proyectos.personales.tienda.products_microservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("com.julian.proyectos.personales.tienda.libs_commons.models")
public class ProductsMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductsMicroserviceApplication.class, args);
	}

}
