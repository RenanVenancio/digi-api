package com.techzone.digi.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.techzone.digi.dto.OrderItemDTO;
import com.techzone.digi.dto.OrderSaleDTO;
import com.techzone.digi.entity.Address;
import com.techzone.digi.entity.Company;
import com.techzone.digi.entity.OrderItem;
import com.techzone.digi.entity.OrderSale;
import com.techzone.digi.entity.Product;
import com.techzone.digi.entity.User;
import com.techzone.digi.enums.OrderStatus;
import com.techzone.digi.enums.PaymentMethod;
import com.techzone.digi.repository.OrderSaleRepository;
import com.techzone.digi.security.UserSS;
import com.techzone.digi.service.exception.AuthorizationException;
import com.techzone.digi.service.exception.DataIntegrityException;
import com.techzone.digi.service.exception.ObjectNotFoundException;

@Service
public class OrderSaleService {

	@Autowired
	private ClientService clientService;

	@Autowired
	private AddressService addressService;

	@Autowired
	private OrderSaleRepository orderSaleRepository;

	@Autowired
	private CompanyService companyService;

	@Autowired
	private ProductService productService;

	public OrderSale findById(Long id) {
		Optional<OrderSale> orderSale = orderSaleRepository.findById(id);
		return orderSale.orElseThrow(() -> new ObjectNotFoundException(OrderSale.class.getName() + " not found"));
	}

	public OrderSale findByIdAndDomain(Long id, String domain) {
		Optional<OrderSale> deliveryArea = orderSaleRepository.findById(id);
		OrderSale obj = deliveryArea
				.orElseThrow(() -> new ObjectNotFoundException(OrderSale.class.getName() + " not found"));
		if (!obj.getCompany().getDomain().equals(domain)) {
			throw new ObjectNotFoundException(OrderSale.class.getName() + " not found");
		}
		return obj;
	}

	public List<Map<String, Object>> salesInPeriod(String companyDomain, Long startDate, Long endDate) {
		List<Map<String, Object>> data = new ArrayList<>();
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
		Date start = null;
		Date end = null;
		Calendar c = Calendar.getInstance();
		if (startDate != null) {
			c.setTime(new Date(startDate));
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			start = c.getTime();
		}
		if (endDate != null) {
			c.setTime(new Date(endDate));
			c.set(Calendar.HOUR_OF_DAY, 23);
			c.set(Calendar.MINUTE, 59);
			c.set(Calendar.SECOND, 59);
			c.set(Calendar.MILLISECOND, 999);
			end = c.getTime();
		}

		Date dt = start;
		c.setTime(start);

		List<OrderSale> orderSales = orderSaleRepository.ordersInPeriod(companyDomain, start, end);
		while (dt.before(end)) {
			Map<String, Object> item = new HashMap<>();
			Date date = new Date(dt.getTime());
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date(dt.getTime()));
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 59);
			cal.set(Calendar.SECOND, 59);
			cal.set(Calendar.MILLISECOND, 999);
			Date finalDate = cal.getTime();
			BigDecimal sum = sumOrders(orderSales.stream()
					.filter((i) -> i.getCreationDate().after(date) && i.getCreationDate().before(finalDate))
					.collect(Collectors.toList()));
			c.add(Calendar.DATE, 1);
			dt = c.getTime();
			item.put("day", formatDate.format(date));
			item.put("value", sum);
			data.add(item);
		}
		return data;

	}

	/**
	 * Retorna a contagem dos status dos pedidos
	 * 
	 * @param companyDomain String: Domínio da empresa
	 * @param startDate     Long: Data inicial do pedido.
	 * @param endDate       Long: Data final do pedido.
	 * @return
	 */
	public Map<String, Object> countStatus(String companyDomain, Long startDate, Long endDate) {
		Map<String, Object> data = new HashMap<>();
		Date start = null;
		Date end = null;

		if (startDate != null) {
			start = new Date(startDate);
		}
		if (endDate != null) {
			end = new Date(endDate);
		}
		long open = orderSaleRepository.countOrderStatus(OrderStatus.OPEN.getCod(), companyDomain, start, end);
		long ready = orderSaleRepository.countOrderStatus(OrderStatus.READY.getCod(), companyDomain, start, end);
		long sent = orderSaleRepository.countOrderStatus(OrderStatus.SENT.getCod(), companyDomain, start, end);
		long finished = orderSaleRepository.countOrderStatus(OrderStatus.FINISHED.getCod(), companyDomain, start, end);
		List<OrderSale> orderSales = orderSaleRepository.ordersInPeriod(companyDomain, start, end);
		data.put("open", open);
		data.put("ready", ready);
		data.put("sent", sent);
		data.put("finished", finished);
		data.put("totals", sumOrders(orderSales));
		return data;
	}

	private BigDecimal sumOrders(List<OrderSale> orderSales) {
		BigDecimal initialValue = new BigDecimal("0.00");
		for (OrderSale orderSale : orderSales) {
			initialValue = initialValue.add(orderSale.getTotal());
		}
		return initialValue;
	}

	@Transactional
	public OrderSale save(OrderSale orderSale) {
		if (orderSale.getPaymentMethod() == null) {
			throw new DataIntegrityException("O método de pagamento deve ser informado.");
		}
		if (orderSale.getClient() == null) {
			throw new DataIntegrityException("Informe o cliente.");
		}
		if (orderSale.getAddress() == null && orderSale.getDelivery() == true) {
			throw new DataIntegrityException("Pedidos para entrega devem conter o endereço.");
		}
		if (orderSale.getDelivery() == true) {
			orderSale.setFreightCost(orderSale.getAddress().getDeliveryArea().getDeliveryValue());
		} else {
			orderSale.setFreightCost(new BigDecimal("0.00"));
		}
		if (orderSale.getClient().getId() == null) {
			User client = orderSale.getClient();
			client = clientService.saveSimple(client);
			orderSale.setClient(client);
		}
		return orderSaleRepository.save(orderSale);
	}

	public Page<OrderSale> search(Integer status, String clientName, Long startDate, Long endDate, String companyDomain,
			Integer page, Integer itensPerPage, String orderBy, String direction) {
		UserSS userAuthenticated = UserService.authenticated();
		if (userAuthenticated == null || !userAuthenticated.isAdmin()
				|| !userAuthenticated.getCompany().getDomain().equals(companyDomain))
			throw new AuthorizationException("Acesso negado.");
		Date start = null;
		Date end = null;
		if (startDate != null) {
			start = new Date(startDate);
		}
		if (endDate != null) {
			end = new Date(endDate);
		}
		PageRequest pageRequest = PageRequest.of(page, itensPerPage, Direction.valueOf(direction), orderBy);
		if (startDate != null) {

		}
		return orderSaleRepository
				.findByStatusAndClientNameAndCreationDateGreaterThanEqualAndCreationDateLessThanEqualAndCompanyDomain(
						status, clientName, start, end, companyDomain, pageRequest);
	}

	public Page<OrderSale> searchByClientName(String clientName, String companyDomain, Integer page,
			Integer itensPerPage, String orderBy, String direction) {
		UserSS userAuthenticated = UserService.authenticated();
		if (userAuthenticated == null || !userAuthenticated.isAdmin()
				|| !userAuthenticated.getCompany().getDomain().equals(companyDomain))
			throw new AuthorizationException("Acesso negado.");
		PageRequest pageRequest = PageRequest.of(page, itensPerPage, Direction.valueOf(direction), orderBy);
		return orderSaleRepository.findByClientNameContainingIgnoreCaseAndCompanyDomain(clientName, companyDomain,
				pageRequest);
	}

	public Page<OrderSale> searchByClientId(UUID clientId, String companyDomain, Integer page, Integer itensPerPage,
			String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, itensPerPage, Direction.valueOf(direction), orderBy);
		return orderSaleRepository.findByClientIdAndCompanyDomain(clientId, companyDomain, pageRequest);
	}

	public List<OrderSale> listAll(String companyDomain) {
		UserSS userAuthenticated = UserService.authenticated();
		if (userAuthenticated == null || !userAuthenticated.isAdmin()
				|| !userAuthenticated.getCompany().getDomain().equals(companyDomain))
			throw new AuthorizationException("Acesso negado.");
		return orderSaleRepository.findAll();
	}

	/**
	 * Responsável por converter um objeto ProductDTO para um objeto Product. Caso
	 * seja informado um cliente já existente no banco de dados o método realiza a
	 * busca e carrega os dados. Caso seja informado um novo cliente, o mesmo será
	 * cadastrado apenas com seus dados básicos(Avulso)
	 * 
	 * @param orderSaleDTO - OrderSaleDTO: Objeto dto instanciado.
	 * @return - Product: Produto pronto para ser salvo no banco de dados.
	 */
	public OrderSale fromDTO(OrderSaleDTO orderSaleDTO) {
		Company company = companyService.findByDomain(orderSaleDTO.getCompanyDomain());
		User client = new User();
		Address address = new Address();
		OrderSale order = new OrderSale();
		List<OrderItem> itens = new ArrayList<>();
		if (orderSaleDTO.getClient().getId() == null) {
			client.setName(orderSaleDTO.getClient().getName());
			client.setPhone(orderSaleDTO.getClient().getPhone());
			client.setEmail(orderSaleDTO.getClient().getEmail());
			address = addressService.fromDTO(orderSaleDTO.getAddress());
			address.setUser(client);
			client.setAddress(Arrays.asList(address));
		} else {
			client = clientService.findById(orderSaleDTO.getClient().getId());
		}
		address = addressService.fromDTO(orderSaleDTO.getAddress());
		order.setObservation(orderSaleDTO.getObservation());
		order.setClient(client);
		order.setAddress(address);
		order.setCompany(company);
		order.setItens(itens);
		order.setDiscount(new BigDecimal("0.00"));
		order.setFreightCost(address.getDeliveryArea().getDeliveryValue());
		order.setDelivery(orderSaleDTO.getDelivery());
		order.setPaymentMethod(PaymentMethod.toEnum(orderSaleDTO.getPaymentMethod()));
		order.setChangeValue(orderSaleDTO.getChangeValue());
		order.setStatus(
				orderSaleDTO.getStatus() != null ? OrderStatus.toEnum(orderSaleDTO.getStatus()) : OrderStatus.OPEN);
		address.setOrderSale(Arrays.asList(order));
		for (OrderItemDTO item : orderSaleDTO.getItens()) {
			Product product = productService.findByIdAndCompanyDomain(item.getProduct(), company.getDomain());
			itens.add(new OrderItem(null, order, product, item.getQuantity(), product.getName(),
					product.getDescription(), item.getObservation(), product.getSalePrice(), product.getCostPrice(),
					product.getAttachment().getId()));
		}
		return order;
	}

}
