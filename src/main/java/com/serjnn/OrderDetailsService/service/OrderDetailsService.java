package com.serjnn.OrderDetailsService.service;


import com.serjnn.OrderDetailsService.dto.BucketItemDTO;
import com.serjnn.OrderDetailsService.dto.OrderDTO;
import com.serjnn.OrderDetailsService.model.OrderDetails;
import com.serjnn.OrderDetailsService.repository.OrderDetailsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderDetailsService {

    private final OrderDetailsRepository orderDetailsRepository;

    public Flux<OrderDetails> findByClientId(long id) {
        log.info("Finding order details for client ID {}", id);
        return orderDetailsRepository.findByClientId(id);
    }



    public Mono<OrderDetails> createOrder(OrderDTO orderDTO) {
        log.info("Creating order: {}", orderDTO);
        OrderDetails orderDetails = new OrderDetails(
                orderDTO.getOrderId(),
                orderDTO.getClientId(),
                this.getProductIds(orderDTO.getItems()),
                orderDTO.getTotalSum());
        return orderDetailsRepository.save(orderDetails)
                .doOnSuccess(saved -> log.info("Successfully saved order details for order ID: {}", orderDTO.getOrderId()));
    }

    private String getProductIds(List<BucketItemDTO> items) {
        return items.stream()
                .map(prod -> prod.getName()
                        .concat(":")
                        .concat(prod.getQuantity().toString())
                        .concat("|"))
                .collect(Collectors.joining());
    }

    public Mono<Void> removeOrder(UUID uuid) {
        log.info("Removing/cancelling order with UUID: {}", uuid);
        return orderDetailsRepository.deleteByUuid(uuid)
                .doOnSuccess(v -> log.info("Successfully deleted order details for UUID: {}", uuid));
    }
}
