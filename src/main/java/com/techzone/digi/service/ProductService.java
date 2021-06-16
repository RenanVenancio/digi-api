package com.techzone.digi.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.techzone.digi.dto.ProductDTO;
import com.techzone.digi.dto.ProductNewDTO;
import com.techzone.digi.entity.Attachment;
import com.techzone.digi.entity.Company;
import com.techzone.digi.entity.Product;
import com.techzone.digi.entity.ProductCategory;
import com.techzone.digi.repository.ProductRepository;
import com.techzone.digi.security.UserSS;
import com.techzone.digi.service.exception.AuthorizationException;
import com.techzone.digi.service.exception.ObjectNotFoundException;

@Service
public class ProductService {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private CompanyService companyService;

	@Autowired
	private ProductCategoryService productCategoryService;

	@Autowired
	private AttachmentService attachmentService;

	/**
	 * Respomsável por salvar um produto no banco de dados.
	 * 
	 * @param product - Instancia do produto.
	 * @return - Produto persistido no banco.
	 */
	public Product save(Product product) {
		UserSS userAuthenticated = UserService.authenticated();
		if (userAuthenticated == null || !userAuthenticated.isAdmin()
				|| userAuthenticated.getCompany().getId() != product.getCompany().getId())
			throw new AuthorizationException("Acesso negado.");
		return productRepository.save(product);
	}

	public Product findByIdAndCompanyDomain(Long id, String companyDomain) {
		Optional<Product> product = productRepository.findByIdAndCompanyDomain(id, companyDomain);
		return product.orElseThrow(() -> new ObjectNotFoundException(Product.class.getName() + " not found"));
	}

	/**
	 * Repsonsável por deletar um produto, para deletar o usuário deve estar logado
	 * e possuir permissões de admin e consequentemente só tem permissão para
	 * deletar produtos da empresa a qual pertence.
	 * 
	 * @param id            - Long: Id do produto.
	 * @param companyDomain - Strig: Domínio da empresa.
	 */
	public void delete(Long id, String companyDomain) {
		checkPermissions(id, companyDomain);
		productRepository.deleteById(id);
	}

	public ArrayList<Product> saveAll(ArrayList<Product> list) {
		return (ArrayList<Product>) productRepository.saveAll(list);
	}
	
	public BigDecimal validateSalePrice(Product obj) {
		if (obj.getSalePrice() != null && obj.getPromotionalValue() != null && obj.getPromotionalValue().signum() != -1
				&& obj.getPromotionValidity() != null) {
			if(obj.getPromotionValidity().before(new Date())) {
				return obj.getPromotionalValue();
			}
		}
		return obj.getSalePrice();
	}

	/**
	 * Responsável por checar preços promocionais, e verificar se a promoção é válida
	 * 
	 * @param obj - Product: Instancia do produto
	 * @return - Product: Produto com preço atualizado
	 */
	public Product validatePromotional(Product obj) {
		if (obj.getSalePrice() != null && obj.getPromotionalValue() != null && obj.getPromotionalValue().signum() != -1
				&& obj.getPromotionValidity() != null) {
			obj.setPromotionValidity(null);
			obj.setPromotionalValue(null);
		}
		return obj;
	}
	
	/**
	 * Responsável por checar preços promocionais, a partir de um objetoDTO
	 * 
	 * @param obj - Product: Instancia do produto
	 * @return - ProductDTO: Produto com preço atualizado
	 */
	public ProductDTO validatePromotionalDTO(Product obj) {
		return new ProductDTO(validatePromotional(obj));
	}

	/**
	 * Método responsável por realizar uma busca paginada de produtos, filtrando
	 * pelo nome
	 * 
	 * @param name         - String: Nome do produto.
	 * @param page         - Integer: Número da página.
	 * @param itensPerPage - Integer: quantidade de itens por página.
	 * @param orderBy      - String: nome do campo a ser ordenado. Ex: "name".
	 * @param direction    - String: "ASC" ou "DESC".
	 * @return
	 */
	public Page<Product> search(String name, Long category, String companyDomain, Integer page, Integer itensPerPage,
			String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, itensPerPage, Direction.valueOf(direction), orderBy);
		if (category.equals(0L)) {
			return productRepository.findDistinctByNameContainingIgnoreCaseAndCompanyDomain(name, companyDomain,
					pageRequest);
		} else {
			return productRepository.findDistinctByNameContainingIgnoreCaseAndProductCategoryIdAndCompanyDomain(name,
					category, companyDomain, pageRequest);
		}
	}

	private Product checkPermissions(Long id, String companyDomain) {
		UserSS userAuthenticated = UserService.authenticated();
		Product product = findByIdAndCompanyDomain(id, companyDomain);
		if (userAuthenticated == null || !userAuthenticated.isAdmin()
				|| userAuthenticated.getCompany().getId() != product.getCompany().getId())
			throw new AuthorizationException("Acesso negado.");
		return product;
	}

	/**
	 * Método responsável por realizar a atualização de um produo já existente. Para
	 * atualizar um produto, um administrador deve estar logado, eo produto deve
	 * pertencer a sua empresa, caso contrário terá seu acesso negado.
	 * 
	 * @param product - Product: Instância do produto com oa dados atualizados.
	 * @return - Product: Produto salvo no banco de dados com seus dados
	 *         atualizados.
	 */
	public Product update(Product product) {
		UserSS userAuthenticated = UserService.authenticated();
		if (userAuthenticated == null || !userAuthenticated.isAdmin()
				|| userAuthenticated.getCompany().getId() != product.getCompany().getId())
			throw new AuthorizationException("Acesso negado.");
		Product updatedProduct = findByIdAndCompanyDomain(product.getId(), userAuthenticated.getCompany().getDomain());
		updateData(updatedProduct, product);
		return productRepository.save(updatedProduct);
	}

	/**
	 * Responsável por realizar a arualização dos dados de um objeto tipo Product
	 * existente.
	 * 
	 * @param oldObj - Product: Instância do produto com os dados antigos.
	 * @param newObj - Product: Instância do produto com os dados atualizados.
	 */
	private void updateData(Product oldObj, Product newObj) {
		oldObj.setName(newObj.getName());
		oldObj.setCostPrice(newObj.getCostPrice());
		oldObj.setSalePrice(newObj.getSalePrice());
		oldObj.setDescription(newObj.getDescription());
		oldObj.setPromotionalValue(newObj.getPromotionalValue());
		oldObj.setPromotionValidity(newObj.getPromotionValidity());
		oldObj.setAttachment(newObj.getAttachment());
		oldObj.setProductCategoty(newObj.getProductCategoty());
	}

	/**
	 * Reponsável por realizar a conversão de um objeto DTO para uma instância de
	 * produto.
	 * 
	 * @param dto - Objeto DTO instanciado.
	 * @return - Instância de Product.
	 */
	public Product fromDTO(ProductDTO dto) {
		Company company = null;
		Attachment attachment = null;
		ProductCategory category = null;
		if (dto.getCompany() != null) {
			company = companyService.findByDomain(dto.getCompany());
		}
		if (dto.getAttachment() != null) {
			attachment = attachmentService.findById(dto.getAttachment());
		}
		if (dto.getProductCategory() != null) {
			category = productCategoryService.findById(dto.getProductCategory().getId());
		}

		Product product = new Product();
		product.setId(dto.getId());
		product.setName(dto.getName());
		product.setDescription(dto.getDescription());
		product.setCostPrice(dto.getCostPrice());
		product.setSalePrice(dto.getSalePrice());
		product.setProductCategoty(category);
		product.setCompany(company);
		product.setAttachment(attachment);
		return product;
	}

	public Product fromDTO(ProductNewDTO dto) {
		Company company = null;
		Attachment attachment = null;
		ProductCategory category = null;
		Product product = new Product();
		if (dto.getCompany() != null) {
			company = companyService.findByDomain(dto.getCompany());
		}
		if (dto.getAttachment() != null) {
			attachment = attachmentService.findById(dto.getAttachment());
		}
		if (dto.getProductCategory() != null) {
			category = productCategoryService.findById(dto.getProductCategory());
		}
		product.setId(dto.getId());
		product.setName(dto.getName());
		product.setDescription(dto.getDescription());
		product.setCostPrice(dto.getCostPrice());
		product.setSalePrice(dto.getSalePrice());
		product.setProductCategoty(category);
		product.setCompany(company);
		product.setAttachment(attachment);

		return product;
	}

}
