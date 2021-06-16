package com.techzone.digi.resource;

import java.net.URI;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.techzone.digi.dto.ClientNewDTO;
import com.techzone.digi.dto.UserDTO;
import com.techzone.digi.entity.User;
import com.techzone.digi.service.ClientService;

@RestController
@RequestMapping(value = "clients/")
public class ClientResource {

	@Autowired
	ClientService clientService;

	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<User> findById(@PathVariable UUID id) {
		User user = clientService.findById(id);
		return ResponseEntity.ok().body(user);
	}

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<UserDTO> loadAuthenticatedUser() {
		User user = clientService.loadAuthenticatedUser();
		
		return ResponseEntity.ok().body(new UserDTO(user));
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Void> save(@RequestBody ClientNewDTO clientDTO) {
		User client = clientService.fromDTO(clientDTO);
		client = clientService.save(client);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(client.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> update(@PathVariable UUID id, @Validated @RequestBody ClientNewDTO clientDTO) {
		User client = clientService.fromDTO(clientDTO);
		client.setId(id);
		client = clientService.update(client);
		return ResponseEntity.noContent().build();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> delete(@PathVariable UUID id, @PathVariable String companyDomain) {
		clientService.delete(id);
		return ResponseEntity.noContent().build();
	}

}
