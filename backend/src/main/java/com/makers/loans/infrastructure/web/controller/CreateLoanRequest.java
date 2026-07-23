package com.makers.loans.infrastructure.web.controller;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateLoanRequest(
        @NotNull(message = "El monto es obligatorio")
        @DecimalMin(value = "0.0", inclusive = false, message = "El monto debe ser mayor que cero")
        @Digits(integer = 17, fraction = 2, message = "El monto debe tener máximo 17 enteros y 2 decimales")
        BigDecimal amount,
        @NotNull(message = "El plazo es obligatorio")
        @Min(value = 1, message = "El plazo debe estar entre 1 y 360 meses")
        @Max(value = 360, message = "El plazo debe estar entre 1 y 360 meses")
        Integer termMonths
) {
}
