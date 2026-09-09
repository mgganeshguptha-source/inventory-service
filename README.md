# inventory-service

Part of the three-service bookstore chain: **book-service → inventory-service → pricing-service** (synchronous REST, WebFlux).

- Java 17, Spring Boot 3.5.0, WebFlux
- Run: `mvn spring-boot:run`
- Contract: `api/`

## Ports
| service | port |
|---|---|
| book-service | 8081 |
| inventory-service | 8082 |
| pricing-service | 8083 |

## Downstream config (env override)
- inventory-service: `PRICING_BASE_URL` (default http://localhost:8083)
- book-service: `INVENTORY_BASE_URL` (default http://localhost:8082)

## Try it (start pricing → inventory → book, then)
```
curl localhost:8081/books/B001   # STANDARD tier, in stock
curl localhost:8081/books/B002   # PREMIUM tier (10% off), in stock
curl localhost:8081/books/B003   # out of stock
```
