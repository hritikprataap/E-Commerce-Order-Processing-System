package com.inventoryMicroservice.inventory_service.Repository;

import com.inventoryMicroservice.inventory_service.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

//jpa takes 2 genric inputs first is the type of object to be stored and its id type;
public interface InventoryRepository extends JpaRepository<Inventory,Long> {

    List<Inventory> findBySkuCodeIn(List<String> skuCode);
}
