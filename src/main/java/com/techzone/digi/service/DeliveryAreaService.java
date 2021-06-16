package com.techzone.digi.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.techzone.digi.dto.DeliveryAreaDTO;
import com.techzone.digi.entity.Company;
import com.techzone.digi.entity.DeliveryArea;
import com.techzone.digi.entity.Product;
import com.techzone.digi.enums.UF;
import com.techzone.digi.repository.DeliveryAreaRepository;
import com.techzone.digi.security.UserSS;
import com.techzone.digi.service.exception.AuthorizationException;
import com.techzone.digi.service.exception.DataIntegrityException;
import com.techzone.digi.service.exception.ObjectNotFoundException;

@Service
public class DeliveryAreaService {

	@Autowired
	DeliveryAreaRepository deliveryAreaRepository;

	@Autowired
	LocationService locationService;

	@Autowired
	CompanyService companyService;

	/**
	 * Método responsável por realizar a busca de uma área de delivery por id.
	 * 
	 * @param id - Id da área de delivery
	 * @return
	 */
	public DeliveryArea findById(Long id) {
		Optional<DeliveryArea> deliveryArea = deliveryAreaRepository.findById(id);
		return deliveryArea.orElseThrow(() -> new ObjectNotFoundException(Product.class.getName() + " not found"));
	}

	/**
	 * Método responsável por consultar a lista de cidades na API do do IBGE
	 * passando o estado como parâmetro e buscar o id da cidade fornecido na
	 * requisição, tendo como objetivo verificar se a cidade pertence ao estado
	 * informado.
	 * 
	 * @param deliveryArea - Instância da área de delivery
	 * @return - Área de delivery persistida no banco.
	 * @throws JsonMappingException
	 * @throws JsonProcessingException
	 */
	public DeliveryArea save(DeliveryArea deliveryArea, String companyDomain)
			throws JsonMappingException, JsonProcessingException {
		if (deliveryArea.getCityIBGEId() == null) {
			throw new DataIntegrityException("O código IBGE da cidade deve ser fornecido.");
		}
		if (deliveryArea.getState() == null) {
			throw new DataIntegrityException("O Estado deve ser informado.");
		}
		if (deliveryArea.getDeliveryValue() == null) {
			throw new DataIntegrityException("O Valor da entrega deve ser informado.");
		}
		if (companyDomain == null) {
			throw new DataIntegrityException("Informe a empresa.");
		}
		Company company = companyService.findByDomain(companyDomain);
		deliveryArea.setCompany(company);
		ArrayList<LinkedHashMap<String, String>> cityMap = locationService.getByUF(deliveryArea.getState());
		Iterator<LinkedHashMap<String, String>> cityIterator = cityMap.iterator();
		Boolean cityFound = false;
		while (cityIterator.hasNext()) {
			LinkedHashMap<String, String> obj = cityIterator.next();
			if (deliveryArea.getCityIBGEId().equals(String.valueOf(obj.get("id")))) {
				deliveryArea.setCity((String) obj.get("nome"));
				cityFound = true;
				break;
			}
		}
		if (cityFound == false) {
			throw new DataIntegrityException("A cidade informada não pertence ao estado " + deliveryArea.getState());
		}
		return deliveryAreaRepository.save(deliveryArea);
	}

	public List<DeliveryArea> findAll(String companyDomain) {
		return deliveryAreaRepository.findByCompanyDomain(companyDomain);
	}

	/**
	 * Método responsável por atualizar uma instancia de DeliveryArea.
	 * 
	 * @param - DeliveryArea: Instância do objeto com os novos dados.
	 * @return - DeliveryArea: Instâncoa do objeto persistido no banco de dados.
	 */
	public DeliveryArea update(DeliveryArea deliveryArea) {
		UserSS userAuthenticated = UserService.authenticated();
		if (userAuthenticated == null || !userAuthenticated.isAdmin()
				|| userAuthenticated.getCompany().getId() != deliveryArea.getCompany().getId())
			throw new AuthorizationException("Acesso negado.");
		DeliveryArea updatedObject = findByIdAndCompanyDomain(deliveryArea.getId(),
				deliveryArea.getCompany().getDomain());
		updateData(updatedObject, deliveryArea);
		return deliveryAreaRepository.save(updatedObject);
	}

	public void delete(Long id, String companyDomain) {
		UserSS userAuthenticated = UserService.authenticated();
		DeliveryArea deliveryArea = findByIdAndCompanyDomain(id, companyDomain);
		if (userAuthenticated == null || !userAuthenticated.isAdmin()
				|| userAuthenticated.getCompany().getId() != deliveryArea.getCompany().getId())
			throw new AuthorizationException("Acesso negado.");
		deliveryAreaRepository.deleteById(id);
	}

	public DeliveryArea findByIdAndCompanyDomain(Long id, String companyDomain) {
		Optional<DeliveryArea> deliveryArea = deliveryAreaRepository.findByIdAndCompanyDomain(id, companyDomain);
		return deliveryArea.orElseThrow(() -> new ObjectNotFoundException(DeliveryArea.class.getName() + " not found"));
	}

	private void updateData(DeliveryArea oldObj, DeliveryArea newObj) {
		oldObj.setCity(newObj.getCity());
		oldObj.setCityIBGEId(newObj.getCityIBGEId());
		oldObj.setCompany(newObj.getCompany());
		oldObj.setDeliveryValue(newObj.getDeliveryValue());
		oldObj.setState(newObj.getState());
	}

	/**
	 * Método responsável por converter um objeto DTO para um objeto DeliveryArea
	 * 
	 * @param deliveryAreaDTO - Instância do objeto DTO.
	 * @return
	 */
	public DeliveryArea fromDTO(DeliveryAreaDTO deliveryAreaDTO) {
		Company company = null;
		if (deliveryAreaDTO.getCompany() != null) {
			company = companyService.findByDomain(deliveryAreaDTO.getCompany());
		}
		return new DeliveryArea(deliveryAreaDTO.getId(), deliveryAreaDTO.getCity(), deliveryAreaDTO.getCityIBGEId(),
				deliveryAreaDTO.getDeliveryValue(), UF.valueOf(deliveryAreaDTO.getState()), company);
	}

}
