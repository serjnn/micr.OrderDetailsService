package com.serjnn.OrderDetailsService.controller;

import com.serjnn.OrderDetailsService.dto.OrderDTO;
import com.serjnn.OrderDetailsService.model.OrderDetails;
import com.serjnn.OrderDetailsService.service.OrderDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
@Tag(name = "Order Details API", description = "Endpoints for managing customer order history")
public class OrderDetailsController {

    private final OrderDetailsService orderDetailsService;

    @GetMapping("/client/{clientId}")
    @Operation(summary = "Get client orders", description = "Retrieve all orders placed by a specific client")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of orders")
    public Flux<OrderDetails> findByClientId(@PathVariable("clientId") Long id) {
        log.info("Request received: Get order details for client ID: {}", id);
        return orderDetailsService.findByClientId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create an order", description = "Record details for a new completed order")
    @ApiResponse(responseCode = "201", description = "Order details successfully recorded")
    public Mono<OrderDetails> save(@RequestBody OrderDTO orderDTO) {
        log.info("Request received: Save order details: {}", orderDTO);
        return orderDetailsService.createOrder(orderDTO);
    }

    @DeleteMapping("/{orderId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete an order", description = "Remove/cancel order details by order UUID (SAGA Compensation)")
    @ApiResponse(responseCode = "204", description = "Order details successfully removed")
    public Mono<Void> remove(@PathVariable("orderId") UUID uuid) {
        log.info("Request received: Cancel/remove order details for UUID: {}", uuid);
        return orderDetailsService.removeOrder(uuid);
    }
}
