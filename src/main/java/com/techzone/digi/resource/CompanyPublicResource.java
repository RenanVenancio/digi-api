package com.techzone.digi.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.techzone.digi.dto.CompanyDTO;
import com.techzone.digi.entity.Company;
import com.techzone.digi.service.CompanyService;

@RestController
@RequestMapping(value = "/companies")
public class CompanyPublicResource {

	@Autowired
	CompanyService companyService;

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<Page<CompanyDTO>> findPage(@RequestParam(value = "domain", defaultValue = "") String domain,
			@RequestParam(value = "name", defaultValue = "") String name,
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "itensPerPage", defaultValue = "24") Integer itensPerPage,
			@RequestParam(value = "orderBy", defaultValue = "id") String orderBy,
			@RequestParam(value = "direction", defaultValue = "ASC") String direction) {
		Page<Company> companyList = companyService.search(domain, name, page, itensPerPage, orderBy, direction);
		Page<CompanyDTO> listDto = companyList.map((obj) -> new CompanyDTO(obj));
		return ResponseEntity.ok().body(listDto);
	}

}
