package com.inventoryMicroservice.inventory_service.Controller;

import com.inventoryMicroservice.inventory_service.Service.InventoryService;
import com.inventoryMicroservice.inventory_service.dto.InventoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    //problem:
    //a customer can add multiple items so checking skucode of each product one by one is not feasable


    //http://localhost:8082/api/inventory?skuCode=iphone-12&sku-code=iphone13-red
    private final InventoryService inventoryService;
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponse> isInStock(@RequestParam List<String> skuCode){
        return  inventoryService.isInStock(skuCode);
    }

}
