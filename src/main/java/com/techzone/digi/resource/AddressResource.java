package com.techzone.digi.resource;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.techzone.digi.dto.AddressDTO;
import com.techzone.digi.entity.Address;
import com.techzone.digi.service.AddressService;

@RestController
@RequestMapping(value = "adresses/")
public class AddressResource {

	@Autowired
	private AddressService addressService;

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Void> save(@RequestBody AddressDTO addressDTO) {
		Address address = addressService.fromDTO(addressDTO);
		address = addressService.save(address);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(address.getId())
				.toUri();
		return ResponseEntity.created(uri).build();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> update(@RequestBody AddressDTO addressDTO, @PathVariable Long id) {
		Address address = addressService.fromDTO(addressDTO);
		address.setId(id);
		address = addressService.update(address);
		return ResponseEntity.noContent().build();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		addressService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<Address>> findAll() {
		return ResponseEntity.ok(addressService.findAll());
	}

}
