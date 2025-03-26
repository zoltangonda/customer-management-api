package com.challenge.customermanagement.integration;

import com.challenge.customermanagement.dto.CreateCustomerRequest;
import com.challenge.customermanagement.dto.CustomerResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CustomerIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testCreateAndRetrieveCustomer() {
        CreateCustomerRequest request = new CreateCustomerRequest("Alice White", "alice@example.com", new BigDecimal("1555"), LocalDate.now().minusMonths(3));
        ResponseEntity<CustomerResponse> postResponse = restTemplate.postForEntity("/customers", request, CustomerResponse.class);

        assertNotNull(postResponse.getBody());
        assertEquals("Alice White", postResponse.getBody().getName());
        assertEquals("Gold", postResponse.getBody().getTier());

        UUID customerId = UUID.fromString(postResponse.getBody().getId());
        ResponseEntity<CustomerResponse> getResponse = restTemplate.getForEntity("/customers/" + customerId, CustomerResponse.class);

        assertNotNull(getResponse.getBody());
        assertEquals("Alice White", getResponse.getBody().getName());
    }

    @Test
    void testUpdateCustomer() {
        CreateCustomerRequest request = new CreateCustomerRequest("Bob Blue", "bob@companyone.com", new BigDecimal("12111"), LocalDate.now().minusMonths(2));
        ResponseEntity<CustomerResponse> postResponse = restTemplate.postForEntity("/customers", request, CustomerResponse.class);

        assertNotNull(postResponse.getBody());
        UUID customerId = UUID.fromString(postResponse.getBody().getId());

        CreateCustomerRequest updatedRequest = new CreateCustomerRequest("Bob Blue", "bob@companyone.com", new BigDecimal("599"), LocalDate.now().minusYears(2));
        HttpEntity<CreateCustomerRequest> requestEntity = new HttpEntity<>(updatedRequest);
        ResponseEntity<CustomerResponse> putResponse = restTemplate.exchange("/customers/" + customerId, HttpMethod.PUT, requestEntity, CustomerResponse.class);

        assertNotNull(putResponse.getBody());
        assertEquals("Silver", putResponse.getBody().getTier());
    }

    @Test
    void testDeleteCustomer() {
        CreateCustomerRequest request = new CreateCustomerRequest("Charlie Hawk", "charlie@example.com", new BigDecimal("7777"), LocalDate.now().minusMonths(10));
        ResponseEntity<CustomerResponse> postResponse = restTemplate.postForEntity("/customers", request, CustomerResponse.class);

        assertNotNull(postResponse.getBody());
        UUID customerId = UUID.fromString(postResponse.getBody().getId());

        restTemplate.delete("/customers/" + customerId);
        ResponseEntity<String> getResponse = restTemplate.getForEntity("/customers/" + customerId, String.class);

        assertEquals(404, getResponse.getStatusCode().value());
    }
}
