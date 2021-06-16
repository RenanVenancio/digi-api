package com.techzone.digi.resource;

import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.techzone.digi.service.LocationService;
import com.techzone.digi.service.exception.ObjectNotFoundException;

@RestController
@RequestMapping(value = "locations/")
public class LocationResource {

	@Autowired
	LocationService ufService;

	/*
	 * @RequestMapping(method = RequestMethod.GET, value = "cities/uf/{uf}") public
	 * ArrayList<LinkedHashMap<String, Object>> findByUf(@PathVariable String uf) {
	 * try { return ufService.getByUF(UF.valueOf(uf.toUpperCase())); } catch
	 * (JsonProcessingException e) { throw new
	 * ObjectNotFoundException("Objeto não enconrado"); } }
	 */
	
	@RequestMapping(method = RequestMethod.GET, value = "city/{id}")
	public LinkedHashMap<String, Object> findByCityId(@PathVariable String id) {
		try {
			return ufService.getByCityId(id);
		} catch (JsonProcessingException e) {
			throw new ObjectNotFoundException("Objeto não enconrado");
		}
	}
}
