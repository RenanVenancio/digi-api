package com.techzone.digi.resource;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.techzone.digi.dto.ProductDTO;
import com.techzone.digi.dto.ProductNewDTO;
import com.techzone.digi.entity.Product;
import com.techzone.digi.service.ProductService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "{companyDomain}/products/")
public class ProductResource {

	@Autowired
	ProductService productService;

	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<ProductDTO> findById(@PathVariable Long id, @PathVariable String companyDomain) {
		Product product = productService.findByIdAndCompanyDomain(id, companyDomain);
		return ResponseEntity.ok().body(new ProductDTO(product));
	}

	@ApiOperation(value = "Salvar um produto no banco de dados (Apenas Admin).")
	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Void> save(@RequestBody ProductNewDTO productDTO, @PathVariable String companyDomain) {
		productDTO.setCompany(companyDomain);
		Product product = productService.fromDTO(productDTO);
		product = productService.save(product);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(product.getId())
				.toUri();
		return ResponseEntity.created(uri).build();
	}

	@ApiOperation(value = "Recuperar um produto no banco de dados (Apenas Admin).")
	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> update(@PathVariable Long id, @Validated @RequestBody ProductNewDTO productDTO,
			@PathVariable String companyDomain) {
		productDTO.setCompany(companyDomain);
		Product product = productService.fromDTO(productDTO);
		product.setId(id);
		productService.update(product);
		return ResponseEntity.noContent().build();
	}

	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> delete(@PathVariable Long id, @PathVariable String companyDomain) {
		productService.delete(id, companyDomain);
		return ResponseEntity.noContent().build();
	}

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<Page<ProductDTO>> findPage(@RequestParam(value = "name", defaultValue = "") String name,
			@RequestParam(value = "category", defaultValue = "0") Long category,
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "itensPerPage", defaultValue = "24") Integer itensPerPage,
			@RequestParam(value = "orderBy", defaultValue = "id") String orderBy,
			@RequestParam(value = "direction", defaultValue = "ASC") String direction,
			@PathVariable String companyDomain) {
		Page<Product> productList = productService.search(name, category, companyDomain, page, itensPerPage, orderBy,
				direction);
		Page<ProductDTO> listDto = productList.map((obj) -> productService.validatePromotionalDTO(obj));
		return ResponseEntity.ok().body(listDto);
	}
}
