package codewithnidhi.order_service.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import codewithnidhi.order_service.client.InventoryClient;
import codewithnidhi.order_service.model.Order;
import codewithnidhi.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
	
	private final OrderRepository orderRepository;
	private final InventoryClient inventoryClient;

	public void placeOrder(OrderRequest orderRequest) {
		var isProductInStock = inventoryClient.isInStock(orderRequest.skuCode(), orderRequest.quantity());
		
		if(isProductInStock) {
		//map orderrequest to order object
		Order order = new Order();
		order.setOrderNumber(UUID.randomUUID().toString());
		order.setPrice(orderRequest.price());
		order.setSkuCode(orderRequest.skuCode());
		order.setQuantity(orderRequest.quantity());
		//save order to orderrequest
		orderRepository.save(order);
		}else {
			throw new RuntimeException("Product with skuCode "+ orderRequest.skuCode()+ " is not in stock");
		}
	}
}
