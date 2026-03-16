package com.serjnn.OrderDetailsService.repo;

import com.serjnn.OrderDetailsService.model.OrderDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class OrderDetailsRepository {

    private final JdbcTemplate jdbcTemplate;

    public List<OrderDetails> findByClientId(long id) {
        String sql = "SELECT * FROM order_details WHERE client_id = ?";
        return jdbcTemplate.query(sql, new DataClassRowMapper<>(OrderDetails.class), id);
    }

    public void save(OrderDetails orderDetails) {
        String sql =
                "INSERT INTO order_details (uuid, client_id, products_ids, sum, created_at) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(
                sql,
                orderDetails.uuid(),
                orderDetails.clientId(),
                orderDetails.productsIds(),
                orderDetails.sum(),
                orderDetails.createdAt()
        );
    }

    public void deleteByUuid(UUID uuid) {
        String sql = "DELETE FROM order_details WHERE uuid = ?";
        jdbcTemplate.update(sql, uuid);
    }
}