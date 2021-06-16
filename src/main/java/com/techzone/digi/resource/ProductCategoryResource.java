package com.techzone.digi.resource;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.techzone.digi.dto.ProductCategoryDTO;
import com.techzone.digi.entity.ProductCategory;
import com.techzone.digi.service.ProductCategoryService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "{companyDomain}/product/categories/")
public class ProductCategoryResource {

	@Autowired
	private ProductCategoryService productCategoryService;

	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<ProductCategoryDTO> findByIdAndDomain(@PathVariable Long id,
			@PathVariable String companyDomain) {
		ProductCategory productCategory = productCategoryService.findByIdAndCompanyDomain(id, companyDomain);
		return ResponseEntity.ok().body(new ProductCategoryDTO(productCategory));
	}

	@ApiOperation(value = "Salvar uma categoria (Apenas Admin).")
	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Void> save(@RequestBody ProductCategoryDTO productCategoryDTO,
			@PathVariable String companyDomain) {
		productCategoryDTO.setCompany(companyDomain);
		ProductCategory productCategory = productCategoryService.fromDTO(productCategoryDTO);
		productCategory = productCategoryService.save(productCategory);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(productCategory.getId())
				.toUri();
		return ResponseEntity.created(uri).build();
	}

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<ProductCategoryDTO>> findAll(@PathVariable String companyDomain) {
		List<ProductCategory> productCategory = productCategoryService.findAllByCompanyDomain(companyDomain);
		List<ProductCategoryDTO> list = productCategory.stream().map((obj) -> new ProductCategoryDTO(obj))
				.collect(Collectors.toList());
		return ResponseEntity.ok().body(list);
	}
}
