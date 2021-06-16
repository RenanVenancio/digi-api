package com.techzone.digi.respository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import com.techzone.digi.repository.OrderSaleRepository;

@AutoConfigureMockMvc
@SpringBootTest
public class OrderSaleRepositoryTest {

	@Autowired
	private OrderSaleRepository orderSaleRepository;

//	@Test
//	public void countOrderStatus() {
//		long counts = orderSaleRepository.countOrderStatus();
//		System.out.println((counts));
//	}

}
