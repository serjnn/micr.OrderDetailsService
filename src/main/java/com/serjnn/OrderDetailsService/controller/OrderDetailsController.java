package com.serjnn.OrderDetailsService.controller;

import com.serjnn.OrderDetailsService.dto.OrderDTO;
import com.serjnn.OrderDetailsService.model.OrderDetails;
import com.serjnn.OrderDetailsService.service.OrderDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderDetailsController {

    private final OrderDetailsService orderDetailsService;

    @GetMapping("/client/{id}")
    public List<OrderDetails> findByClientId(@PathVariable("id") Long id) {
        return orderDetailsService.findByClientId(id);
    }

    @PostMapping
    public void save(@RequestBody OrderDTO orderDTO) {
        orderDetailsService.createOrder(orderDTO);
    }

    @DeleteMapping("/{uuid}")
    public void remove(@PathVariable("uuid") UUID uuid) {
        orderDetailsService.removeOrder(uuid);
    }
}