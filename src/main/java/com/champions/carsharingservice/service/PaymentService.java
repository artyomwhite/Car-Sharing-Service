package com.champions.carsharingservice.service;

import com.champions.carsharingservice.dto.CreatePaymentRequestDto;
import com.champions.carsharingservice.dto.PaymentDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface PaymentService {
    List<PaymentDto> getPayments(Long userId, Pageable pageable);

    List<PaymentDto> getPaymentsByStatus(Long userId, String status, Pageable pageable);

    PaymentDto createPaymentSession(CreatePaymentRequestDto request);

    PaymentDto getSuccessfulPayment(String sessionId);

    PaymentDto getCancelledPayment(String sessionId);
}
