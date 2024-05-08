package com.champions.carsharingservice.dto;

import com.champions.carsharingservice.model.Payment;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreatePaymentRequestDto(
        @Positive
        Long rentalId,
        @NotNull
        Payment.PaymentType paymentType
) {

}
