package codewithnidhi.order_service.service;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import codewithnidhi.order_service.client.InventoryClient;
import codewithnidhi.order_service.event.OrderPlacedEvent;
import codewithnidhi.order_service.model.Order;
import codewithnidhi.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
	
	private static final Logger log = LoggerFactory.getLogger(OrderService.class);
	private final OrderRepository orderRepository;
	private final InventoryClient inventoryClient;
	private final KafkaTemplate<String,OrderPlacedEvent> kafkaTemplate;

	public void placeOrder(OrderRequest orderRequest) {
		var isProductInStock = inventoryClient.isInStock(orderRequest.skuCode(), orderRequest.quantity());
		
		if(isProductInStock) {
		//map orderrequest to order object
		Order order = new Order();
		order.setOrderNumber(UUID.randomUUID().toString());
		order.setPrice(orderRequest.price());
		order.setSkuCode(orderRequest.skuCode());
		order.setQuantity(orderRequest.quantity());
		//save order to order request
		orderRepository.save(order);
		//send the message to kafka topic
		OrderPlacedEvent orderPlacedEvent = new OrderPlacedEvent();
		orderPlacedEvent.setOrderNumber(order.getOrderNumber());
		orderPlacedEvent.setEmail(orderRequest.userDetails().email());
		orderPlacedEvent.setFirstName(orderRequest.userDetails().firstName());
		orderPlacedEvent.setLastName(orderRequest.userDetails().lastName());
		//orderNumber, email
		log.info("start-sending orderplaced event {} to kafka topic", orderPlacedEvent);
		kafkaTemplate.send("order-placed", orderPlacedEvent);
		log.info("end-sending orderplaced event {} to kafka topic", orderPlacedEvent);
		}else {
			throw new RuntimeException("Product with skuCode "+ orderRequest.skuCode()+ " is not in stock");
		}
	}
}
