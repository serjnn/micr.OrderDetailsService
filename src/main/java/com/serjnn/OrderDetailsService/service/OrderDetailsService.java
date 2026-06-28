package com.serjnn.OrderDetailsService.service;

import com.serjnn.OrderDetailsService.dto.BucketItemDTO;
import com.serjnn.OrderDetailsService.dto.OrderDTO;
import com.serjnn.OrderDetailsService.model.OrderDetails;
import com.serjnn.OrderDetailsService.repository.OrderDetailsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderDetailsService {

    private final OrderDetailsRepository orderDetailsRepository;

    public List<OrderDetails> findByClientId(long id) {
        log.info("Finding order details for client ID {}", id);
        return orderDetailsRepository.findByClientId(id);
    }

    public void createOrder(OrderDTO orderDTO) {
        log.info("Creating order: {}", orderDTO);
        OrderDetails orderDetails = new OrderDetails(
                orderDTO.orderId(),
                orderDTO.clientId(),
                this.getProductIds(orderDTO.items()),
                orderDTO.totalSum());
        orderDetailsRepository.save(orderDetails);
        log.info("Successfully saved order details for order ID: {}", orderDTO.orderId());
    }

    private String getProductIds(List<BucketItemDTO> items) {
        return items.stream()
                .map(prod -> prod.name()
                        .concat(":")
                        .concat(prod.quantity().toString())
                        .concat("|"))
                .collect(Collectors.joining());
    }

    public void removeOrder(UUID uuid) {
        log.info("Removing/cancelling order with UUID: {}", uuid);
        orderDetailsRepository.deleteByUuid(uuid);
        log.info("Successfully deleted order details for UUID: {}", uuid);
    }
}