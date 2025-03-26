package com.challenge.customermanagement.controller;

import com.challenge.customermanagement.dto.CreateCustomerRequest;
import com.challenge.customermanagement.dto.CustomerResponse;
import com.challenge.customermanagement.dto.UpdateCustomerRequest;
import com.challenge.customermanagement.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@Validated
@RequestMapping("/customers")
@RequiredArgsConstructor
@Tag(name = "Customer", description = "CRUD operations for customers")
public class CustomerController {
    private final CustomerService customerService;

    @Operation(
            summary = "Create a new customer",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Customer successfully created"),
                    @ApiResponse(responseCode = "400", description = "Invalid input or validation error")
            }
    )
    @PostMapping
    public ResponseEntity<CustomerResponse> createCustomer(@Valid @RequestBody CreateCustomerRequest request) {
        return ResponseEntity.ok(customerService.createCustomer(request));
    }

    @Operation(
            summary = "Get customer by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Customer retrieved successfully"),
                    @ApiResponse(responseCode = "404", description = "Customer not found")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable UUID id) {
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    @Operation(
            summary = "Get all customers",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of all customers returned successfully")
            }
    )
    @GetMapping
    public ResponseEntity<List<CustomerResponse>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @Operation(
            summary = "Get customers by name",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Customer(s) retrieved successfully")
            }
    )
    @GetMapping(params = "name")
    public ResponseEntity<List<CustomerResponse>> getCustomersByName(@RequestParam String name) {
        return ResponseEntity.ok(customerService.getCustomersByName(name));
    }

    @Operation(
            summary = "Get customers by email",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Customer(s) retrieved successfully")
            }
    )
    @GetMapping(params = "email")
    public ResponseEntity<List<CustomerResponse>> getCustomersByEmail(
            @RequestParam @Email(message = "Invalid email format") String email) {
        return ResponseEntity.ok(customerService.getCustomersByEmail(email));
    }

    @Operation(
            summary = "Update customer details",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Customer successfully updated"),
                    @ApiResponse(responseCode = "400", description = "Invalid input or validation error"),
                    @ApiResponse(responseCode = "404", description = "Customer not found")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> updateCustomer(@PathVariable UUID id, @Valid @RequestBody UpdateCustomerRequest request) {
        return ResponseEntity.ok(customerService.updateCustomer(id, request));
    }

    @Operation(
            summary = "Delete customer by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Customer successfully deleted"),
                    @ApiResponse(responseCode = "404", description = "Customer not found")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteCustomer(@PathVariable UUID id) {
    customerService.deleteCustomer(id);
    Map<String, String> response = new HashMap<>();
    response.put("message", "Customer deleted successfully");
    return ResponseEntity.ok(response); // Use 200 OK with body
    }

}
