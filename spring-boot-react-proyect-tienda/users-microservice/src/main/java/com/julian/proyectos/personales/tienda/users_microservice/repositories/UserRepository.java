package com.julian.proyectos.personales.tienda.users_microservice.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.julian.proyectos.personales.tienda.users_microservice.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {

	@Query("""
			SELECT u
			FROM User u
			WHERE CAST(u.id AS string) = :value
			   OR u.username = :value
			""")
	Optional<User> findUserByIdOrUsername(String value);

	boolean existsByUsername(String username);

	boolean existsByEmail(String email);

}
