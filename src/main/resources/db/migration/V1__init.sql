CREATE TABLE order_details (
    id SERIAL PRIMARY KEY,
    uuid UUID,
    client_id BIGINT,
    products_ids VARCHAR(255),
    sum NUMERIC,
    created_at TIMESTAMP
);