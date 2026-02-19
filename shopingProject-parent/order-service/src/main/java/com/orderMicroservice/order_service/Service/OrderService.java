package com.orderMicroservice.order_service.Service;

import com.orderMicroservice.order_service.Repository.OrderRepository;
import com.orderMicroservice.order_service.dto.OrderLineItemsDto;
import com.orderMicroservice.order_service.dto.OrderRequest;
import com.orderMicroservice.order_service.model.Order;
import com.orderMicroservice.order_service.model.OrderLineItems;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    public void placeOrder(OrderRequest orderRequest){
        Order order=new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapTodto)
                .toList();

        order.setOrderLineItemsList(orderLineItems);

        //calls inventory service checking if the order is in stock or not


        orderRepository.save(order);

    }

    private OrderLineItems mapTodto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems=new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;

    }

}
