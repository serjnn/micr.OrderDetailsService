package com.serjnn.OrderDetailsService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.serjnn.OrderDetailsService.dto.BucketItemDTO;
import com.serjnn.OrderDetailsService.dto.OrderDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Disabled("Disabled by default because running Testcontainers requires a local Docker daemon (e.g. Docker Desktop) to be active.")
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class OrderDetailsControllerIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DROP TABLE IF EXISTS order_details;");
        jdbcTemplate.execute("CREATE TABLE order_details (" +
                "id SERIAL PRIMARY KEY, " +
                "uuid UUID, " +
                "client_id BIGINT, " +
                "products_ids VARCHAR(255), " +
                "sum NUMERIC, " +
                "created_at TIMESTAMP" +
                ");");
    }

    @Test
    void shouldCreateOrderAndFindByClientId() throws Exception {
        // Given
        UUID orderId = UUID.randomUUID();
        long clientId = 101L;
        OrderDTO orderDTO = new OrderDTO(
                orderId,
                clientId,
                List.of(new BucketItemDTO(1L, "TestProduct", 2, new BigDecimal("10.50"))),
                new BigDecimal("21.00")
        );

        // When - Create Order
        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderDTO)))
                .andExpect(status().isCreated());

        // Then - Find by Client ID
        mockMvc.perform(get("/api/v1/orders/client/" + clientId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].uuid").value(orderId.toString()))
                .andExpect(jsonPath("$[0].clientId").value(clientId))
                .andExpect(jsonPath("$[0].productsIds").value("TestProduct:2|"))
                .andExpect(jsonPath("$[0].sum").value(21.0));
    }

    @Test
    void shouldRemoveOrder() throws Exception {
        // Given
        UUID orderId = UUID.randomUUID();
        long clientId = 102L;
        OrderDTO orderDTO = new OrderDTO(
                orderId,
                clientId,
                List.of(new BucketItemDTO(2L, "Product2", 1, new BigDecimal("50.00"))),
                new BigDecimal("50.00")
        );

        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderDTO)))
                .andExpect(status().isCreated());

        // When - Remove Order
        mockMvc.perform(delete("/api/v1/orders/" + orderId))
                .andExpect(status().isNoContent());

        // Then - Verify it's deleted
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM order_details WHERE uuid = ?", Integer.class, orderId);
        assertEquals(0, count);
    }
}