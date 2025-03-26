package com.challenge.customermanagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCustomerRequest {
    @Schema(description = "Customer's full name")
    private String name;

    @Schema(description = "Customer's email address")
    @Email(message = "Invalid email format")
    private String email;

    @Schema(description = "Annual amount spent by the customer", example = "5000.00")
    private BigDecimal annualSpend;

    @Schema(description = "Date of the customer's last purchase", example = "2023-08-15")
    private LocalDate lastPurchaseDate;
}
