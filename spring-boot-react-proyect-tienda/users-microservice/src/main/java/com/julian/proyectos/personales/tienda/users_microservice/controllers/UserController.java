package com.julian.proyectos.personales.tienda.users_microservice.controllers;

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

import com.julian.proyectos.personales.tienda.users_microservice.entities.User;
import com.julian.proyectos.personales.tienda.users_microservice.services.UserService;

@RestController
public class UserController {

	@Autowired
	private UserService service;

	@GetMapping
	public ResponseEntity<List<User>> findAllUsers() {
		return ResponseEntity.ok(service.findAll());
	}

	@GetMapping("/{value}")
	public ResponseEntity<?> findUserByIdOrUsername(@PathVariable String value) {
		Optional<User> userOpt = service.findUserByIdOrUsername(value);
		return userOpt.isPresent() ? ResponseEntity.ok(userOpt.get())
				: ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(Collections.singletonMap("message", "El usuario no existe!"));
	}

	@PostMapping
	public ResponseEntity<?> saveUser(@RequestBody User user) {
		if (!service.existsUserByEmail(user.getEmail())) {
			return service.findUserByIdOrUsername(user.getUsername()).isEmpty()
					? ResponseEntity.status(HttpStatus.CREATED).body(service.saveUser(user))
					: ResponseEntity.status(HttpStatus.CONFLICT).body(Collections.singletonMap("message",
							"El username '" + user.getUsername() + "' esta en uso, pruebe con otro!"));
		}
		return ResponseEntity.status(HttpStatus.CONFLICT).body(Collections.singletonMap("message",
				"El email '" + user.getEmail() + "' esta en uso, pruebe con otro!"));

	}

	@PutMapping("/{entry}")
	public ResponseEntity<?> updateUserByIdOrUsername(@PathVariable String entry, @RequestBody User user) {
		Optional<User> userOpt = service.findUserByIdOrUsername(entry);
		if (userOpt.isPresent()) {
			boolean isUsernameUniqueOrEqual = !service.existsUserByUsername(user.getUsername())
					|| user.getUsername().equals(userOpt.get().getUsername());
			boolean isEmailUniqueOrEqual = !service.existsUserByEmail(user.getEmail())
					|| user.getEmail().equals(userOpt.get().getEmail());
			boolean isValidUsernameAndPassword = isUsernameUniqueOrEqual && isEmailUniqueOrEqual;
			return isValidUsernameAndPassword
					? ResponseEntity.status(HttpStatus.CREATED).body(service.updateUserByIdOrUsername(entry, user))
					: ResponseEntity.status(HttpStatus.CONFLICT)
							.body(Collections.singletonMap("message",
									"El " + (!isUsernameUniqueOrEqual ? "username" : "email") + " '"
											+ (!isUsernameUniqueOrEqual ? user.getUsername() : user.getEmail())
											+ "' esta en uso, pruebe con otro!"));
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(Collections.singletonMap("message", "El usuario no existe!"));

	}

	@DeleteMapping("/{entry}")
	public ResponseEntity<?> deleteUserByIdOrUsername(@PathVariable String entry) {
		if (service.findUserByIdOrUsername(entry).isPresent()) {
			service.deleteUserByIdOrUsername(entry);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(Collections.singletonMap("message", "El usuario no existe!"));
	}

}