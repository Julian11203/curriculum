package com.julian.proyectos.personales.tienda.users_microservice.services;

import java.util.List;
import java.util.Optional;

import com.julian.proyectos.personales.tienda.users_microservice.entities.User;

public interface UserService {
	List<User> findAll();

	Optional<User> findUserByIdOrUsername(String value);

	Optional<User> saveUser(User user);

	Optional<User> updateUserByIdOrUsername(String value, User user);

	void deleteUserByIdOrUsername(String value);

	boolean existsUserByUsername(String username);

	boolean existsUserByEmail(String email);
}
