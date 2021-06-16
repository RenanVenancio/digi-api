package com.techzone.digi.repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.techzone.digi.entity.OrderSale;

public interface OrderSaleRepository extends JpaRepository<OrderSale, Long> {
	Page<OrderSale> findByClientNameContainingIgnoreCaseAndCompanyDomain(String clientName, String companyDomain,
			Pageable pageRequest);

	Page<OrderSale> findByClientIdAndCompanyDomain(UUID clientId, String companyDomain, Pageable pageRequest);

	Page<OrderSale> findByStatusAndClientNameAndCreationDateGreaterThanEqualAndCreationDateLessThanEqualAndCompanyDomain(
			Integer status, String clientName, Date startDate, Date endDate, String companyDomain,
			Pageable pageRequest);
	
	@Query("SELECT COUNT(o.status) FROM OrderSale o WHERE o.status=:status AND o.company.domain=:domain and o.creationDate >= :startDate AND o.creationDate <= :endDate")
	long countOrderStatus(@Param("status") Integer status, @Param("domain") String domain, @Param("startDate") Date startDat, @Param("endDate") Date endDate);

	@Query("SELECT o FROM OrderSale o WHERE o.company.domain=:domain and o.creationDate >= :startDate AND o.creationDate <= :endDate")
    List<OrderSale> ordersInPeriod(@Param("domain") String domain, @Param("startDate") Date startDat, @Param("endDate") Date endDate);

}
