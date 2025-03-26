# Customer Management API

A Spring Boot RESTful service for managing customer data, supporting full CRUD operations, membership tier logic, and in-memory testing with H2.

---

## How to Build and Run the Application

### Prerequisites

- Java 21+
- Maven 3+

### Run Locally

1. **Clone the repository:**

   ```bash
   git clone https://github.com/zoltangonda/customer-management-api.git
   cd customer-management-api
   ```

2. **Build the application:**

   ```bash
   mvn clean install
   ```

3. **Start the application:**

   ```bash
   mvn spring-boot:run
   ```

4. Application runs on:\
   `http://localhost:8080`

---

## API Documentation

Swagger UI is available at:\
[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

## Sample Requests

### Create a Customer

```http
POST /customers
Content-Type: application/json

{
  "name": "Vicky White",
  "email": "vicky@samplecompany.com",
  "annualSpend": 2874.00,
  "lastPurchaseDate": "2024-08-22"
}
```

### Get Customer by ID

```http
GET /customers/{id}
```

### Search by Name or Email

```http
GET /customers?name=Vicky
GET /customers?email=vicky@samplecompany.com
```

### Update a Customer

```http
PUT /customers/{id}
Content-Type: application/json

{
  "annualSpend": 8265.00
}
```

### Delete a Customer

```http
DELETE /customers/{id}
```

---

## Membership Tiers

Customer tier is calculated based on `annualSpend` and `lastPurchaseDate`:

| Tier     | Condition                                                          |
| -------- | ------------------------------------------------------------------ |
| Silver   | Annual Spend < \$1000                                              |
| Gold     | \$1000 ≤ Annual Spend < \$10,000 and last purchase ≤ 12 months ago |
| Platinum | Annual Spend ≥ \$10,000 and last purchase ≤ 6 months ago           |

Tier is **calculated dynamically** and not stored in the database.

---

## H2 Database Console

You can inspect data via H2 console:

- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:customerdb`
- Username: `sa`
- Password: *(leave blank)*

---

## Assumptions Made

- The `id` field is generated server-side and must **not** be provided in customer creation requests. The system rejects such requests.
- All tier logic is stateless and based on the current date.
- No external authentication is implemented — this is a public API for the purpose of the challenge.
- Only basic email validation and field presence checks are implemented via annotations and manual checks.

---

## Author

**Zoltan Gonda**\
[GitHub Profile](https://github.com/zoltangonda)
