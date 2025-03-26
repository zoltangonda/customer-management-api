package com.challenge.customermanagement.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.challenge.customermanagement.dto.CreateCustomerRequest;
import com.challenge.customermanagement.dto.CustomerResponse;
import com.challenge.customermanagement.dto.UpdateCustomerRequest;
import com.challenge.customermanagement.exception.CustomerNotFoundException;
import com.challenge.customermanagement.model.Customer;
import com.challenge.customermanagement.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class CustomerServiceTest {
    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    private Customer customer;
    private CreateCustomerRequest createCustomerRequest;
    private UpdateCustomerRequest updateCustomerRequest;
    private UUID customerId;

    @BeforeEach
    void setUp() {
        customerId = UUID.randomUUID();
        customer = Customer.builder()
                .id(customerId)
                .name("John Doe")
                .email("johndoe@sample.com")
                .annualSpend(new BigDecimal("5321"))
                .lastPurchaseDate(LocalDate.now().minusMonths(5))
                .build();

        createCustomerRequest = new CreateCustomerRequest("John Doe", "johndoe@sample.com", new BigDecimal("5321"), LocalDate.now().minusMonths(5));

        updateCustomerRequest = new UpdateCustomerRequest(null, null, new BigDecimal("7600"), null);
    }

    @Test
    void testCreateCustomer() {
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        CustomerResponse response = customerService.createCustomer(createCustomerRequest);

        assertNotNull(response);
        assertEquals("John Doe", response.getName());
        assertEquals("johndoe@sample.com", response.getEmail());
        assertEquals("Gold", response.getTier());
    }

    @Test
    void testGetCustomerById_CustomerExists() {
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        CustomerResponse response = customerService.getCustomerById(customerId);

        assertNotNull(response);
        assertEquals("John Doe", response.getName());
    }

    @Test
    void testGetCustomerById_CustomerNotFound() {
        lenient().when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(CustomerNotFoundException.class, () -> customerService.getCustomerById(customerId));
        assertEquals("Customer not found", exception.getMessage());
    }

    @Test
    void testGetAllCustomers() {
        Customer customer1 = Customer.builder()
                .id(UUID.randomUUID())
                .name("John Doe")
                .email("john@companyone.com")
                .annualSpend(new BigDecimal("2350"))
                .lastPurchaseDate(LocalDate.now().minusMonths(4))
                .build();

        Customer customer2 = Customer.builder()
                .id(UUID.randomUUID())
                .name("Jane Smith")
                .email("jane@checkit.com")
                .annualSpend(new BigDecimal("11700"))
                .lastPurchaseDate(LocalDate.now().minusMonths(2))
                .build();

        when(customerRepository.findAll()).thenReturn(List.of(customer1, customer2));

        List<CustomerResponse> customers = customerService.getAllCustomers();

        assertEquals(2, customers.size());
        assertEquals("Gold", customers.get(0).getTier());
        assertEquals("Platinum", customers.get(1).getTier());

        verify(customerRepository, times(1)).findAll();
    }

    @Test
    void testGetCustomersByEmail_NoResults() {
        when(customerRepository.findAllByEmail("nonexistent@company.com")).thenReturn(List.of());

        List<CustomerResponse> response = customerService.getCustomersByEmail("nonexistent@company.com");

        assertTrue(response.isEmpty());
    }

    @Test
    void testGetCustomersByName_NoResults() {
        when(customerRepository.findAllByName("Nonexistent Name")).thenReturn(List.of());

        List<CustomerResponse> response = customerService.getCustomersByName("Nonexistent Name");

        assertTrue(response.isEmpty());
    }


    @Test
    void testUpdateCustomer() {
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        CustomerResponse response = customerService.updateCustomer(customerId, updateCustomerRequest);

        assertNotNull(response);
        assertEquals("John Doe", response.getName());

        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void testDeleteCustomer_CustomerNotFound() {
        when(customerRepository.existsById(customerId)).thenReturn(false);

        Exception exception = assertThrows(CustomerNotFoundException.class, () -> customerService.deleteCustomer(customerId));
        assertEquals("Customer not found", exception.getMessage());

        verify(customerRepository, times(1)).existsById(customerId);
    }

    @Test
    void testCalculateTier_Silver() {
        Customer freshCustomer = new Customer(UUID.randomUUID(), "Ian Evans", "IanEvans@yahoo.com", new BigDecimal("588"), LocalDate.now());
        String tier = customerService.calculateTier(freshCustomer);
        assertEquals("Silver", tier);
    }

    @Test
    void testCalculateTier_Gold() {
        Customer freshCustomer = new Customer(UUID.randomUUID(), "Peter Guzman", "peter.guzman@gmail.com", new BigDecimal("5166"), LocalDate.now().minusMonths(6));
        String tier = customerService.calculateTier(freshCustomer);
        assertEquals("Gold", tier);
    }

    @Test
    void testCalculateTier_Platinum() {
        Customer freshCustomer = new Customer(UUID.randomUUID(), "Erica Free", "erica.free@company.com", new BigDecimal("15555"), LocalDate.now().minusMonths(3));
        String tier = customerService.calculateTier(freshCustomer);
        assertEquals("Platinum", tier);
    }
}
