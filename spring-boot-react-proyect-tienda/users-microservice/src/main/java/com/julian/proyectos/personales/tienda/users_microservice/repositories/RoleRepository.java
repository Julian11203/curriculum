package com.julian.proyectos.personales.tienda.users_microservice.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.julian.proyectos.personales.tienda.users_microservice.entities.Role;

public interface RoleRepository extends JpaRepository<Role, Long>{

	Optional<Role> findByName(String name);

}
