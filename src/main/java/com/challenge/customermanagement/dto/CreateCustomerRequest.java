package com.challenge.customermanagement.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = false)
public class CreateCustomerRequest {

    @Schema(hidden = true)
    private String id; //trap field to catch forbidden usage

    @Schema(description = "Customer full name", required = true)
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;

    @Schema(description = "Customer email", required = true)
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @Schema(description = "Annual amount spent by the customer", example = "5000.00")
    @DecimalMin(value = "0.0", inclusive = true, message = "Annual spend must be a positive value")
    private BigDecimal annualSpend;

    @Schema(description = "Date of the customer's last purchase", example = "2023-08-15")
    private LocalDate lastPurchaseDate;

    public CreateCustomerRequest(String name, String email, BigDecimal annualSpend, LocalDate lastPurchaseDate) {
        this.name = name;
        this.email = email;
        this.annualSpend = annualSpend;
        this.lastPurchaseDate = lastPurchaseDate;
    }
}
