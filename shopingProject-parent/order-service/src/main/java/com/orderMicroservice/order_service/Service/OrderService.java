package com.orderMicroservice.order_service.Service;

import com.orderMicroservice.order_service.Repository.OrderRepository;
import com.orderMicroservice.order_service.dto.InventoryResponse;
import com.orderMicroservice.order_service.dto.OrderLineItemsDto;
import com.orderMicroservice.order_service.dto.OrderRequest;
import com.orderMicroservice.order_service.event.OrderPlacedEvent;
import com.orderMicroservice.order_service.model.Order;
import com.orderMicroservice.order_service.model.OrderLineItems;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;
    private final KafkaTemplate<String,OrderPlacedEvent> kafkaTemplate;


    public String placeOrder(OrderRequest orderRequest) throws IllegalAccessException {
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

        InventoryResponse[] inventoryResponses = webClientBuilder.build().get()
                .uri("http://inventory-service/api/inventory",
                uriBuilder -> uriBuilder.queryParam("skuCode",skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();


        //check if all products are in stock
        boolean allproductsInStock = Arrays.stream(inventoryResponses)
                 .allMatch(InventoryResponse::isInStock);


        if(allproductsInStock){
            orderRepository.save(order);
            kafkaTemplate.send("notificationTopic",new OrderPlacedEvent(order.getOrderNumber()));
            return "order placed Successfully";
        }
        else{
             throw new IllegalAccessException("product is not in stock try again after sometime");
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
