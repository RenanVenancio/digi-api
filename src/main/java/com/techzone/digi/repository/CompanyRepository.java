package com.techzone.digi.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.techzone.digi.entity.Company;

public interface CompanyRepository extends JpaRepository<Company, Long> {

	Optional<Company> findByDomain(String domain);

	@Transactional(readOnly = true)
	Page<Company> findDistinctByNameContainingIgnoreCase(String name, Pageable pageRequest);

	Page<Company> findDistinctByDomain(String domain, Pageable pageRequest);

}
