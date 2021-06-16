package com.techzone.digi.resource;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.techzone.digi.dto.UserNewDTO;
import com.techzone.digi.entity.User;
import com.techzone.digi.service.UserService;

@RestController
@RequestMapping(value = "{companyDomain}/users/")
public class UserResource {

	@Autowired
	private UserService userService;

	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Void> save(@RequestBody UserNewDTO userDTO, @PathVariable String companyDomain) {
		userDTO.setCompany(companyDomain);
		User user = userService.fromDTO(userDTO);
		user = userService.save(user);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user.getId()).toUri();
		return ResponseEntity.created(uri).build();

	}
}
