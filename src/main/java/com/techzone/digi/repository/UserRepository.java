package com.techzone.digi.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techzone.digi.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	User findByEmail(String email);

	Optional<User> findById(UUID id);
}
