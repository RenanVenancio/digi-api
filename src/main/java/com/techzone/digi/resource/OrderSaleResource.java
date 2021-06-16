package com.techzone.digi.resource;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.techzone.digi.dto.OrderSaleDTO;
import com.techzone.digi.entity.OrderSale;
import com.techzone.digi.service.OrderSaleService;

@RestController
@RequestMapping(value = "{companyDomain}/order/")
public class OrderSaleResource {

	@Autowired
	OrderSaleService orderSaleService;

	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<OrderSale> findByIdAndDomain(@PathVariable Long id, @PathVariable String companyDomain) {
		OrderSale orderSale = orderSaleService.findByIdAndDomain(id, companyDomain);
		return ResponseEntity.ok().body(orderSale);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/orderStatistics")
	public ResponseEntity<Map<String, Object>> orderStatistics(@PathVariable String companyDomain,
			@RequestParam(value = "startDate", defaultValue = "") Long startDate,
			@RequestParam(value = "endDate", defaultValue = "") Long endDate) {
		Map<String, Object> orderSale = orderSaleService.countStatus(companyDomain, startDate, endDate);
		return ResponseEntity.ok().body(orderSale);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/orderTotals")
	public ResponseEntity<List<Map<String, Object>>> orderTotals(@PathVariable String companyDomain,
			@RequestParam(value = "startDate", defaultValue = "") Long startDate,
			@RequestParam(value = "endDate", defaultValue = "") Long endDate) {
		List<Map<String, Object>> orderSale = orderSaleService.salesInPeriod(companyDomain, startDate, endDate);
		return ResponseEntity.ok().body(orderSale);
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<OrderSaleDTO> save(@RequestBody OrderSaleDTO orderSaleDTO,
			@PathVariable String companyDomain) {
		orderSaleDTO.setCompanyDomain(companyDomain);
		OrderSale orderSale = orderSaleService.fromDTO(orderSaleDTO);
		orderSale = orderSaleService.save(orderSale);
		return ResponseEntity.ok(new OrderSaleDTO(orderSale));
	}

	@RequestMapping(method = RequestMethod.GET, value = "/search/")
	public ResponseEntity<Page<OrderSale>> search(@RequestParam(value = "status", defaultValue = "") Integer status,
			@RequestParam(value = "clientName", defaultValue = "") String clientName,
			@RequestParam(value = "startDate") Long startDate, @RequestParam(value = "endDate") Long endDate,
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "itensPerPage", defaultValue = "24") Integer itensPerPage,
			@RequestParam(value = "orderBy", defaultValue = "id") String orderBy,
			@RequestParam(value = "direction", defaultValue = "ASC") String direction,
			@PathVariable String companyDomain) {
		Page<OrderSale> orderList = orderSaleService.search(status, clientName, startDate, endDate, companyDomain, page,
				itensPerPage, orderBy, direction);
		return ResponseEntity.ok().body(orderList);
	}

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<Page<OrderSale>> findPage(@RequestParam(value = "name", defaultValue = "") String name,
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "itensPerPage", defaultValue = "24") Integer itensPerPage,
			@RequestParam(value = "orderBy", defaultValue = "id") String orderBy,
			@RequestParam(value = "direction", defaultValue = "ASC") String direction,
			@PathVariable String companyDomain) {
		Page<OrderSale> orderList = orderSaleService.searchByClientName(name, companyDomain, page, itensPerPage,
				orderBy, direction);
		return ResponseEntity.ok().body(orderList);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/client/{clientId}")
	public ResponseEntity<Page<OrderSale>> findByClientId(@RequestParam(value = "name", defaultValue = "") String name,
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "itensPerPage", defaultValue = "24") Integer itensPerPage,
			@RequestParam(value = "orderBy", defaultValue = "id") String orderBy,
			@RequestParam(value = "direction", defaultValue = "ASC") String direction, @PathVariable UUID clientId,
			@PathVariable String companyDomain) {
		Page<OrderSale> orderList = orderSaleService.searchByClientId(clientId, companyDomain, page, itensPerPage,
				orderBy, direction);
		return ResponseEntity.ok().body(orderList);
	}

}
