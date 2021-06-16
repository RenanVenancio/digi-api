package com.techzone.digi.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.techzone.digi.enums.UF;

@Service
public class LocationService {

	@SuppressWarnings("unchecked")
	public ArrayList<LinkedHashMap<String, String>> getByUF(UF uf) throws JsonMappingException, JsonProcessingException {
		String url = "https://servicodados.ibge.gov.br/api/v1/localidades/estados/" + uf + "/municipios";
		RestTemplate restTemplate = new RestTemplate();
		ArrayList<LinkedHashMap<String, String>> response = restTemplate.getForObject(url, ArrayList.class);
		return response;
	}
	
	@SuppressWarnings("unchecked")
	public LinkedHashMap<String, Object> getByCityId(String id) throws JsonMappingException, JsonProcessingException {
		String url = "https://servicodados.ibge.gov.br/api/v1/localidades/municipios/" + id;
		RestTemplate restTemplate = new RestTemplate();
		LinkedHashMap<String, Object> response = restTemplate.getForObject(url, LinkedHashMap.class);
		return response;
	}
	
}
