package com.challenge.customermanagement.service;

import com.challenge.customermanagement.dto.CreateCustomerRequest;
import com.challenge.customermanagement.dto.CustomerResponse;
import com.challenge.customermanagement.dto.UpdateCustomerRequest;
import com.challenge.customermanagement.exception.CustomerNotFoundException;
import com.challenge.customermanagement.model.Customer;
import com.challenge.customermanagement.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerResponse createCustomer(CreateCustomerRequest request) {
        if (request.getId() != null) {
            throw new IllegalArgumentException("The 'id' field must not be included in the request body.");
        }
        if (request.getName() == null || request.getName().isBlank()) {
            throw new IllegalArgumentException("Customer 'name' is required and cannot be blank.");
        }
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new IllegalArgumentException("Customer 'email' is required and cannot be blank.");
        }

        Customer customer = Customer.builder()
                .name(request.getName())
                .email(request.getEmail())
                .annualSpend(request.getAnnualSpend())
                .lastPurchaseDate(request.getLastPurchaseDate())
                .build();
        Customer savedCustomer = customerRepository.save(customer);
        return mapToResponse(savedCustomer);
    }

    public CustomerResponse updateCustomer(UUID id, UpdateCustomerRequest request) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));

        if (request.getName() != null) customer.setName(request.getName());
        if (request.getEmail() != null) customer.setEmail(request.getEmail());
        if (request.getAnnualSpend() != null) customer.setAnnualSpend(request.getAnnualSpend());
        if (request.getLastPurchaseDate() != null) customer.setLastPurchaseDate(request.getLastPurchaseDate());

        return mapToResponse(customerRepository.save(customer));
    }

    public CustomerResponse getCustomerById(UUID id) {
        return customerRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
    }

    public List<CustomerResponse> getAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<CustomerResponse> getCustomersByName(String name) {
        return customerRepository.findAllByName(name)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<CustomerResponse> getCustomersByEmail(String email) {
        return customerRepository.findAllByEmail(email)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public void deleteCustomer(UUID id) {
        if (!customerRepository.existsById(id)) {
            throw new CustomerNotFoundException("Customer not found");
        }
        customerRepository.deleteById(id);
    }

    private CustomerResponse mapToResponse(Customer customer) {
        return new CustomerResponse(
                customer.getId().toString(),
                customer.getName(),
                customer.getEmail(),
                customer.getAnnualSpend(),
                customer.getLastPurchaseDate(),
                calculateTier(customer)
        );
    }

    public String calculateTier(Customer customer) {
        if (customer.getAnnualSpend() == null) {
            return "Silver";
        }
        BigDecimal spend = customer.getAnnualSpend();
        if (spend.compareTo(new BigDecimal("10000")) >= 0 && customer.getLastPurchaseDate() != null &&
                ChronoUnit.MONTHS.between(customer.getLastPurchaseDate(), LocalDate.now()) <= 6) {
            return "Platinum";
        } else if (spend.compareTo(new BigDecimal("1000")) >= 0 && spend.compareTo(new BigDecimal("10000")) < 0 &&
                customer.getLastPurchaseDate() != null &&
                ChronoUnit.MONTHS.between(customer.getLastPurchaseDate(), LocalDate.now()) <= 12) {
            return "Gold";
        }
        return "Silver";
    }
}
