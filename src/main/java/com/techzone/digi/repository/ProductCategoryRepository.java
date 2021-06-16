package com.techzone.digi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techzone.digi.entity.ProductCategory;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {
	Optional<ProductCategory> findByIdAndCompanyDomain(Long id, String companyDomain);

	List<ProductCategory> findByCompanyDomain(String companyDomain);
}
