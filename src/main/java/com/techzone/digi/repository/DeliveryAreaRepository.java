package com.techzone.digi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techzone.digi.entity.DeliveryArea;

public interface DeliveryAreaRepository extends JpaRepository<DeliveryArea, Long> {
	List<DeliveryArea> findByCompanyDomain(String company);

	Optional<DeliveryArea> findByIdAndCompanyDomain(Long id, String companyDomain);
}
