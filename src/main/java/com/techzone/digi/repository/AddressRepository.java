package com.techzone.digi.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techzone.digi.entity.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {
	List<Address> findAllByUserId(UUID id);
}
