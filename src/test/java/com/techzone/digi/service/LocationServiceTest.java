package com.techzone.digi.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.techzone.digi.entity.DeliveryArea;
import com.techzone.digi.enums.UF;

public class LocationServiceTest {
	@Test
	public void getCity() throws JsonMappingException, JsonProcessingException {
		DeliveryArea deliveryArea = new DeliveryArea();
		deliveryArea.setCityIBGEId("2500106");
		LocationService ufService = new LocationService();
		ArrayList<LinkedHashMap<String, String>> cityMap = ufService.getByUF(UF.PB);
		Iterator<LinkedHashMap<String, String>> cityIterator = cityMap.iterator();
		while (cityIterator.hasNext()) {
			LinkedHashMap<String, String> obj = cityIterator.next();
			String id = String.valueOf(obj.get("id"));
			if (id.equals(deliveryArea.getCityIBGEId())) {
				deliveryArea.setCity((String) obj.get("nome"));
			}

		}
	}
}
