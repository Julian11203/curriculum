package com.julian.proyectos.personales.tienda.users_microservice.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.julian.proyectos.personales.tienda.users_microservice.entities.Role;
import com.julian.proyectos.personales.tienda.users_microservice.entities.User;
import com.julian.proyectos.personales.tienda.users_microservice.enums.RoleName;
import com.julian.proyectos.personales.tienda.users_microservice.repositories.RoleRepository;
import com.julian.proyectos.personales.tienda.users_microservice.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	@Transactional(readOnly = true)
	public List<User> findAll() {
		return userRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<User> findUserByIdOrUsername(String value) {
		return userRepository.findUserByIdOrUsername(value);
	}

	@Override
	@Transactional
	public Optional<User> saveUser(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setRoles(getRoles(user));
		return Optional.of(userRepository.save(user));
	}

	@Override
	@Transactional
	public Optional<User> updateUserByIdOrUsername(String value, User user) {
		Optional<User> userDbOpt = userRepository.findUserByIdOrUsername(value);
		if (userDbOpt.isPresent()) {
			User userDb = userDbOpt.get();
			userDb.setUsername(user.getUsername());
			userDb.setPassword(passwordEncoder.encode(user.getPassword()));
			userDb.setEmail(user.getEmail());
			userDb.setRoles(getRoles(user));
			userDb.setEnabled(user.isEnabled());
			return Optional.of(userRepository.save(userDb));
		}
		return Optional.empty();
	}

	@Override
	@Transactional
	public void deleteUserByIdOrUsername(String value) {
		Optional<User> userOpt = userRepository.findUserByIdOrUsername(value);
		if (userOpt.isPresent()) {
			User userDb = userOpt.get();
			userRepository.deleteById(userDb.getId());
		}
	}
	
	@Override
	public boolean existsUserByUsername(String username) {
		return userRepository.existsByUsername(username);
	}

	@Override
	public boolean existsUserByEmail(String email) {
		return userRepository.existsByEmail(email);
	}

	private List<Role> getRoles(User user) {
		List<Role> listRoles = new ArrayList<>();
		Optional<Role> roleUser = roleRepository.findByName(RoleName.ROLE_USER.name());
		Optional<Role> roleAdmin = roleRepository.findByName(RoleName.ROLE_ADMIN.name());
		if (roleUser.isPresent()) {
			listRoles.add(roleUser.get());
		}
		if (user.isAdmin() && roleAdmin.isPresent()) {
			listRoles.add(roleAdmin.get());
		}
		return listRoles;
	}


}
