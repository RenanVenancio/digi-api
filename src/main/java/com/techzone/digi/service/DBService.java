package com.techzone.digi.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.techzone.digi.entity.Address;
import com.techzone.digi.entity.Attachment;
import com.techzone.digi.entity.Company;
import com.techzone.digi.entity.DeliveryArea;
import com.techzone.digi.entity.Product;
import com.techzone.digi.entity.ProductCategory;
import com.techzone.digi.entity.User;
import com.techzone.digi.enums.UF;
import com.techzone.digi.repository.AttachmentRepository;
import com.techzone.digi.repository.CompanyRepository;
import com.techzone.digi.repository.DeliveryAreaRepository;
import com.techzone.digi.repository.ProductCategoryRepository;
import com.techzone.digi.repository.ProductRepository;
import com.techzone.digi.repository.UserRepository;

/**
 * Classe responsável por criar dados no banco de dados H2 para realização de
 * testes.
 * 
 * @author Venancio
 *
 */

@Service
public class DBService {

	@Autowired
	private ProductRepository produRepository;

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private DeliveryAreaRepository deliveryAreaRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ProductCategoryRepository productCategoryRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private AttachmentRepository attachmentRepository;

	public void initDatabase() {
		Address addr = new Address(null, "Rua Manoel Limeira Snatos, 444", "Centro", "Em frente ao campo", "58140999",
				"São Sebastião do Umbuzeiro", UF.PB, null);
		Company c = new Company(null, "venanciolanches.com", "09784029000198", "Lanchonete do Renan",
				"venaniolanches@gmail.com", true, null, Arrays.asList(addr), null, null);
		addr.setCompany(c);
		DeliveryArea deliveryArea = new DeliveryArea(null, "São Sebastião do Umbuzeiro", "2515203",
				new BigDecimal("4.65"), UF.PB, c);

		Address addr2 = new Address(null, "Joaquim Quintana Melo, 4324A", "Centro", "Rua do jardim", "90830999",
				"Damião", UF.PB, null);
		String[] phones = { "(83)3233-0999", "(83)99134-0900", "(83)3245-6678" };
		Company c2 = new Company(null, "japalanches.com", "09784029000198", "Comida Japonesa do Japa",
				"japal@gmail.com", true, new HashSet<>(Arrays.stream(phones).collect(Collectors.toSet())),
				Arrays.asList(addr2), null, null);
		DeliveryArea deliveryArea2 = new DeliveryArea(null, "Damião", "2505352", new BigDecimal("3.50"), UF.PB, c2);
		addr2.setCompany(c2);
		Address addr3 = new Address(null, "Rua Jose Lira, 431", "Centro", "Teste", "58140999",
				"São Sebastião do Umbuzeiro", UF.PB, null);
		User user = new User(null, "Renan Venacio", "renan_1419@hotmail.com", bCryptPasswordEncoder.encode("1234"),
				true, "999303000", Arrays.asList(addr3), c);
		addr3.setUser(user);

		companyRepository.saveAll(Arrays.asList(c, c2));
		deliveryAreaRepository.saveAll(Arrays.asList(deliveryArea, deliveryArea2));
		userRepository.save(user);

		// PERSISTINDO IMAGENS DOS PRODUTOS

		Attachment fotoBurguer = new Attachment(null, "burg", "jpg", true, c);
		Attachment fotoCoca = new Attachment(null, "coca", "jpg", true, c);
		Attachment fotoMolho = new Attachment(null, "molho", "jpg", true, c);
		Attachment fotoPepsi = new Attachment(null, "pepsi", "jpg", true, c2);
		Attachment fotoCQuente = new Attachment(null, "cquente", "jpg", true, c2);
		Attachment fotoSushi = new Attachment(null, "sushi", "jpg", true, c2);
		Attachment fotoCoxinha = new Attachment(null, "coxinha", "jpg", true, c2);

		Attachment salgadosCategory = new Attachment(null, "coxinha", "jpg", true, c2);
		Attachment bebidasCategory = new Attachment(null, "coxinha", "jpg", true, c2);
		Attachment sanduichesCategory = new Attachment(null, "coxinha", "jpg", true, c2);
		Attachment friosCategory = new Attachment(null, "coxinha", "jpg", true, c2);
		Attachment japonesaCategory = new Attachment(null, "coxinha", "jpg", true, c);
		Attachment sobremesaCategory = new Attachment(null, "coxinha", "jpg", true, c2);
		Attachment bebidasCategoryC = new Attachment(null, "coxinha", "jpg", true, c);
		Attachment sanduichesCategoryC = new Attachment(null, "coxinha", "jpg", true, c);
		Attachment japaLanchesLogo = new Attachment(null, "logo", "png", true, c2);
		Attachment fotoMisto = new Attachment(null, "fotomisto", "jpg", true, c2);
		Attachment fotoPastel = new Attachment(null, "fotopastel", "jpg", true, c2);
		Attachment fotoTapioca = new Attachment(null, "fototapioca", "jpg", true, c2);
		Attachment fotoPao = new Attachment(null, "fotopao", "jpg", true, c2);
		Attachment fotoCafe = new Attachment(null, "fotocafe", "jpg", true, c2);
		Attachment fotoMousse = new Attachment(null, "fotomousse", "jpg", true, c2);
		Attachment fotoPudim = new Attachment(null, "fotopudim", "jpg", true, c2);
		attachmentRepository.saveAll(Arrays.asList(fotoBurguer, fotoCoca, fotoMolho, fotoPepsi, fotoCQuente, fotoSushi,
				fotoCoxinha, salgadosCategory, bebidasCategory, sanduichesCategory, friosCategory, japonesaCategory,
				sobremesaCategory, bebidasCategoryC, sanduichesCategoryC, japaLanchesLogo, fotoMisto, fotoPastel,
				fotoTapioca, fotoPao, fotoCafe, fotoMousse, fotoPudim));
		c2.setLogo(japaLanchesLogo);
		companyRepository.save(c2);

		// CATEGORIAS DE PRODUTOS
		ProductCategory pc = new ProductCategory(null, "Bebidas", c2, bebidasCategory);
		ProductCategory pc1 = new ProductCategory(null, "Sanduiches", c2, sanduichesCategory);
		ProductCategory pc2 = new ProductCategory(null, "Frios", c2, friosCategory);
		ProductCategory pc3 = new ProductCategory(null, "Sobremesas", c2, sobremesaCategory);
		ProductCategory pc4 = new ProductCategory(null, "Sanduiches", c, sanduichesCategoryC);
		ProductCategory pc5 = new ProductCategory(null, "Japonesa", c2, japonesaCategory);
		ProductCategory pc6 = new ProductCategory(null, "Bebidas", c, bebidasCategoryC);

		productCategoryRepository.saveAll(Arrays.asList(pc, pc1, pc2, pc3, pc4, pc5, pc6));

		// PERSISTINDO PRODUTOS

		ArrayList<Product> productList = new ArrayList<>();
		Product p = new Product(null, "Hamburguer Big", "Melhor Burguer da casa", new BigDecimal("3.65"),
				new BigDecimal("9.99"), null, null, pc4, c, fotoBurguer);
		productList.add(p);
		p = new Product(null, "Coca cola 500ML", "Refrigerante de Cola", new BigDecimal("2.34"), new BigDecimal("5.99"),
				null, null, pc6, c, fotoCoca);
		productList.add(p);
		p = new Product(null, "Xícara de Café 300ML", "Café com açúcar, com a possbilidade de adicionar leite",
				new BigDecimal("2.34"), new BigDecimal("3.99"), null, null, pc, c2, fotoCafe);
		productList.add(p);
		p = new Product(null, "Molho Especial", "Molho especial da casa", new BigDecimal("1.34"),
				new BigDecimal("3.12"), null, null, pc4, c, fotoMolho);
		productList.add(p);
		p = new Product(null, "Pepsi Lata 350ML", "Latinha pepsi geladinha", new BigDecimal("2.34"),
				new BigDecimal("3.99"), null, null, pc, c2, fotoPepsi);
		productList.add(p);
		p = new Product(null, "Cachorro quente completo", "Cachorro quente completo com molho", new BigDecimal("4.64"),
				new BigDecimal("7.99"), null, null, pc1, c2, fotoCQuente);
		productList.add(p);
		p = new Product(null, "Sushi Mix", "Sushi completo com brinde especial", new BigDecimal("23.35"),
				new BigDecimal("32.99"), null, null, pc5, c2, fotoSushi);
		productList.add(p);
		p = new Product(null, "Coxinha de Frando", "Coxinha de frango recheada com catupiry", new BigDecimal("3.35"),
				new BigDecimal("3.99"), null, null, pc1, c2, fotoCoxinha);
		productList.add(p);
		p = new Product(null, "Misto quente", "Delicioso misto quente com mussarela e presundo na chapa",
				new BigDecimal("4.35"), new BigDecimal("5.99"), null, null, pc1, c2, fotoMisto);
		productList.add(p);
		p = new Product(null, "Pastel frito diversos Sabores", "Pastel frito sequinho, escolha até 3 sabores",
				new BigDecimal("4.35"), new BigDecimal("4.19"), null, null, pc1, c2, fotoPastel);
		productList.add(p);
		p = new Product(null, "Tapioca Recheada", "Recheios deleciosos", new BigDecimal("4.35"), new BigDecimal("7.99"),
				null, null, pc1, c2, fotoTapioca);
		productList.add(p);
		p = new Product(null, "Pão francês com manteiga", "Ideal para o seu café da manhã", new BigDecimal("1.35"),
				new BigDecimal("2.49"), null, null, pc1, c2, fotoPao);
		productList.add(p);
		p = new Product(null, "Mousse de Maracujá", "Porção com 259g", new BigDecimal("1.35"), new BigDecimal("3.49"),
				null, null, pc3, c2, fotoMousse);
		productList.add(p);
		p = new Product(null, "Fatia de pudim", "Porção com 350g", new BigDecimal("1.35"), new BigDecimal("5.49"), null,
				null, pc3, c2, fotoPudim);
		productList.add(p);
		produRepository.saveAll(productList);

	}
}
