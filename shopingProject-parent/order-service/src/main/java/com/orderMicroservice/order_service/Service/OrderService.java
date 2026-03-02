package com.orderMicroservice.order_service.Service;

import com.orderMicroservice.order_service.Repository.OrderRepository;
import com.orderMicroservice.order_service.exception.InventoryServiceUnavailableException;
import com.orderMicroservice.order_service.exception.ProductNotInStockException;
import com.orderMicroservice.order_service.dto.InventoryResponse;
import com.orderMicroservice.order_service.dto.OrderLineItemsDto;
import com.orderMicroservice.order_service.dto.OrderRequest;
import com.orderMicroservice.order_service.event.OrderPlacedEvent;
import com.orderMicroservice.order_service.model.Order;
import com.orderMicroservice.order_service.model.OrderLineItems;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;
    private final KafkaTemplate<String,OrderPlacedEvent> kafkaTemplate;


    public String placeOrder(OrderRequest orderRequest) {
        Order order=new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        //converting dto->entity
        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapTodto)
                .toList();

        //saving all the order items to list
        order.setOrderLineItemsList(orderLineItems);

        //collecting all the skucode from the customers cart
        List<String> skuCodes = order.getOrderLineItemsList().stream()
                .map(OrderLineItems::getSkuCode).toList();

        //calls inventory service checking if the order is in stock or not
        //bodyToMono converts the json reponse to the java array here

        InventoryResponse[] inventoryResponses;
        try {
            inventoryResponses = webClientBuilder.build().get()
                    .uri("http://inventory-service/api/inventory",
                            uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                    .retrieve()
                    .bodyToMono(InventoryResponse[].class)
                    .block();
        } catch (WebClientRequestException ex) {
            throw new InventoryServiceUnavailableException("Inventory service is unreachable", ex);
        }

        if (inventoryResponses == null) {
            throw new InventoryServiceUnavailableException("Inventory service returned an empty response");
        }


        //check if all products are in stock
        Set<String> requestedSkuCodes = Set.copyOf(skuCodes);
        Map<String, Boolean> stockBySkuCode = new HashMap<>();
        Arrays.stream(inventoryResponses).forEach(response ->
                stockBySkuCode.merge(response.getSkuCode(), response.isInStock(), Boolean::logicalOr));

        boolean allproductsInStock = requestedSkuCodes.stream()
                .allMatch(skuCode -> Boolean.TRUE.equals(stockBySkuCode.get(skuCode)));


        if(allproductsInStock){
            orderRepository.save(order);
            kafkaTemplate.send("notificationTopic",new OrderPlacedEvent(order.getOrderNumber()));
            return "order placed Successfully";
        }
        else{
             throw new ProductNotInStockException("product is not in stock try again after sometime");
        }




    }

    //dto->entity
    private OrderLineItems mapTodto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems=new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;

    }

}
