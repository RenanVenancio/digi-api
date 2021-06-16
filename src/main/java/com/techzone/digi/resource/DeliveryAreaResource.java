package com.techzone.digi.resource;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.techzone.digi.dto.DeliveryAreaDTO;
import com.techzone.digi.entity.DeliveryArea;
import com.techzone.digi.service.CompanyService;
import com.techzone.digi.service.DeliveryAreaService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/{companyDomain}/deliveryArea/")
public class DeliveryAreaResource {

	@Autowired
	DeliveryAreaService deliveryAreaService;

	@Autowired
	CompanyService companyService;

	@ApiOperation(value = "Salvar nova area de delivery (Apenas Admin).")
	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Void> save(@RequestBody DeliveryAreaDTO deliveryAreaDTO, @PathVariable String companyDomain)
			throws JsonMappingException, JsonProcessingException {
		DeliveryArea deliveryArea = deliveryAreaService.fromDTO(deliveryAreaDTO);
		deliveryArea = deliveryAreaService.save(deliveryArea, companyDomain);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(deliveryArea.getId())
				.toUri();
		return ResponseEntity.created(uri).build();
	}

	@RequestMapping(method = RequestMethod.GET)
	public List<DeliveryArea> findAll(@PathVariable String companyDomain) {
		List<DeliveryArea> deliveryAreaList = deliveryAreaService.findAll(companyDomain);
		return deliveryAreaList;
	}

	@ApiOperation(value = "Recuperar uma área de delivery no banco de dados (Apenas Admin).")
	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> update(@PathVariable Long id, @Validated @RequestBody DeliveryAreaDTO deliveryAreaDTO,
			@PathVariable String companyDomain) {
		deliveryAreaDTO.setCompany(companyDomain);
		DeliveryArea deliveryArea = deliveryAreaService.fromDTO(deliveryAreaDTO);
		deliveryArea.setId(id);
		deliveryAreaService.update(deliveryArea);
		return ResponseEntity.noContent().build();
	}

	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> delete(@PathVariable Long id, @PathVariable String companyDomain) {
		deliveryAreaService.delete(id, companyDomain);
		return ResponseEntity.noContent().build();
	}

}
