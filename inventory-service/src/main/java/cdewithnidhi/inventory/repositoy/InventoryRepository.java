package cdewithnidhi.inventory.repositoy;

import org.springframework.data.jpa.repository.JpaRepository;

import cdewithnidhi.inventory.model.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory, Long>{

	boolean existsBySkuCodeAndQuantityIsGreaterThanEqual(String skuCode, Integer quantity);

}
