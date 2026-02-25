package com.inventoryMicroservice.inventory_service.Service;

import com.inventoryMicroservice.inventory_service.Repository.InventoryRepository;
import com.inventoryMicroservice.inventory_service.dto.InventoryResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;


@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    @SneakyThrows
    //this sneakyThrowsException must not be present in production code
    public List<InventoryResponse> isInStock(List<String> skuCode){
//        log.info("Wait Strated");
//        Thread.sleep(10000);
//        log.info("wait ended");
        return inventoryRepository.findBySkuCodeIn(skuCode).stream()
                //this map means build inventoryResponse obj from inventory in the database
                //and make a list of all the inventory response
                .map(inventory ->
                    InventoryResponse.builder()
                            .skuCode(inventory.getSkuCode())
                            .isInStock(inventory.getQuantity()>0)
                            .build()
                ).toList();
    }
}

