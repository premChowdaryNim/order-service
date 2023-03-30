package com.prem.orderservice.service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.prem.orderservice.dto.InventoryResponse;
import com.prem.orderservice.dto.OrderLineItemsDto;
import com.prem.orderservice.dto.OrderRequest;
import com.prem.orderservice.event.OrderPlacedEvent;
import com.prem.orderservice.model.Order;
import com.prem.orderservice.model.OrderLineItems;
import com.prem.orderservice.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {
	
	private final OrderRepository orderRepository;
	private final WebClient.Builder webClientBuilder;
	private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;
	
    public void placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapToDto)
                .toList();

        order.setOrderLineItemsList(orderLineItems);
        List<String> skuCodes = order.getOrderLineItemsList().stream()
                .map(OrderLineItems::getSkuCode)
                .toList();

       InventoryResponse[] inventoryResponsArray = webClientBuilder.build().get()
    		   .uri("http://Inventory-service/api/inventory",
                       uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
              .retrieve()
		      .bodyToMono(InventoryResponse[].class)
		      .block();
       
       boolean allProductsInStock = Arrays.stream(inventoryResponsArray)
               .allMatch(InventoryResponse::isInStock);

       if (allProductsInStock) {
        orderRepository.save(order);
        kafkaTemplate.send("notificationTopic",new OrderPlacedEvent(order.getOrderNumber()));
       }
        else
        	throw new IllegalArgumentException("Product is not in stock, Please try again later");
    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;
    }
}
