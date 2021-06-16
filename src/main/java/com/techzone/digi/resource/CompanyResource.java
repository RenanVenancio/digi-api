package com.techzone.digi.resource;

import java.net.URI;

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

import com.techzone.digi.dto.CompanyDTO;
import com.techzone.digi.entity.Company;
import com.techzone.digi.service.CompanyService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/{companyDomain}/companies")
public class CompanyResource {

	@Autowired
	CompanyService companyService;

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<CompanyDTO> findByDomain(@PathVariable String companyDomain) {
		Company company = companyService.findByDomain(companyDomain);
		return ResponseEntity.ok().body(new CompanyDTO(company));
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Void> save(@RequestBody CompanyDTO companyDTO) {
		Company company = companyService.fromDTO(companyDTO, false);
		company = companyService.save(company);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(company.getId())
				.toUri();
		return ResponseEntity.created(uri).build();
	}

	@ApiOperation(value = "Atualizar dados da empresa (Apenas Admin).")
	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(method = RequestMethod.PUT)
	public ResponseEntity<Void> update(@Validated @RequestBody CompanyDTO companyDTO,
			@PathVariable String companyDomain) {
		Company company = companyService.fromDTO(companyDTO, true);
		companyService.update(company);
		return ResponseEntity.noContent().build();
	}
}
