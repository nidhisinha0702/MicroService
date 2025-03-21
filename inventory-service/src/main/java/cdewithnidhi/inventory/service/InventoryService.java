package cdewithnidhi.inventory.service;

import org.springframework.stereotype.Service;

import cdewithnidhi.inventory.repositoy.InventoryRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryService {

	private final InventoryRepository inventoryRepository;
	
	public boolean isInStrock(String skuCode, Integer quantity) {
		//find an inventory for a given skuCode where quantity >= 0
		return inventoryRepository.existsBySkuCodeAndQuantityIsGreaterThanEqual(skuCode,quantity);
	}
}
