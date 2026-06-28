# OrderDetailsService

This microservice is responsible for managing order details and history. It stores client orders, including products, total sum, and timestamps.

## API Documentation

The service exposes the following endpoints:

### Order Endpoints

#### Get Orders by Client ID
- **URL**: `/api/v1/orders/client/{id}`
- **Method**: `GET`
- **Path Variable**: `id` (Long) - The ID of the client.
- **Response**: `200 OK` - List of `OrderDetails`.

#### Save New Order
- **URL**: `/api/v1/orders`
- **Method**: `POST`
- **Request Body**: `OrderDTO`
    ```json
    {
      "orderId": "uuid",
      "clientId": 1,
      "items": [
        {
          "id": 1,
          "name": "Product Name",
          "quantity": 2,
          "price": 99.99
        }
      ],
      "totalSum": 199.98
    }
    ```
- **Response**: `200 OK`.

#### Remove Order
- **URL**: `/api/v1/orders/{uuid}`
- **Method**: `DELETE`
- **Path Variable**: `uuid` (UUID) - The unique identifier of the order.
- **Response**: `200 OK`.

## Service Interactions

- **PostgreSQL**: Used for persistent storage of order details.
- **Flyway**: Manages database migrations.
- **Eureka**: Registers with service discovery to be reachable by other services.


