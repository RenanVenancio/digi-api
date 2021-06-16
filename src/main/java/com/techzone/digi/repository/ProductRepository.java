package com.techzone.digi.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.techzone.digi.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
	@Transactional(readOnly = true)
	Page<Product> findDistinctByNameContainingIgnoreCaseAndProductCategoryIdAndCompanyDomain(String name, Long category, String companyDomain,
			Pageable pageRequest);
	
	Page<Product> findDistinctByNameContainingIgnoreCaseAndCompanyDomain(String name, String companyDomain,
			Pageable pageRequest);

	Optional<Product> findByIdAndCompanyDomain(Long id, String companyDomain);
}
