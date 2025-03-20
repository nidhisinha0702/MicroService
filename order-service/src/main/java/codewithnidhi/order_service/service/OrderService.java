package codewithnidhi.order_service.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import codewithnidhi.order_service.model.Order;
import codewithnidhi.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {
	
	private final OrderRepository orderRepository;

	public void placeOrder(OrderRequest orderRequest) {
		//map orderrequest to order object
		Order order = new Order();
		order.setOrderNumber(UUID.randomUUID().toString());
		order.setPrice(orderRequest.price());
		order.setSkuCode(orderRequest.skuCode());
		order.setQuantity(orderRequest.quantity());
		//save order to orderrequest
		orderRepository.save(order);
	}
}
