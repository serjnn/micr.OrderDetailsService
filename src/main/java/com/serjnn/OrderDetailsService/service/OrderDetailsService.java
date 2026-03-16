package com.serjnn.OrderDetailsService.service;

import com.serjnn.OrderDetailsService.dto.BucketItemDTO;
import com.serjnn.OrderDetailsService.dto.OrderDTO;
import com.serjnn.OrderDetailsService.model.OrderDetails;
import com.serjnn.OrderDetailsService.repo.OrderDetailsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderDetailsService {

    private final OrderDetailsRepository orderDetailsRepository;

    public List<OrderDetails> findByClientId(long id) {
        return orderDetailsRepository.findByClientId(id);
    }

    public void createOrder(OrderDTO orderDTO) {
        OrderDetails orderDetails = new OrderDetails(
                null,
                orderDTO.orderId(),
                orderDTO.clientId(),
                getProductIds(orderDTO.items()),
                orderDTO.totalSum(),
                LocalDateTime.now());
        orderDetailsRepository.save(orderDetails);
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
        orderDetailsRepository.deleteByUuid(uuid);
    }
}