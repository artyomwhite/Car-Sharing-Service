package com.champions.carsharingservice.controller;

import com.champions.carsharingservice.dto.CreatePaymentRequestDto;
import com.champions.carsharingservice.dto.PaymentDto;
import com.champions.carsharingservice.model.User;
import com.champions.carsharingservice.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/payments")
@Tag(name = "Payment management", description = "Endpoints for managing payments")
public class PaymentController {
    private final PaymentService paymentService;

    @ResponseBody
    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping
    @Operation(summary = "Get all user's payments",
            description = """
                    Get all user's payments.
                    Pageable default: page = 0, size = 10""")
    public List<PaymentDto> getAll(@PageableDefault(page = 0, size = 10) Pageable pageable,
                                   Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return paymentService.getPayments(user.getId(), pageable);
    }

    @ResponseBody
    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/search/")
    @Operation(summary = "Get all payments by status",
            description = """
                    Get all payments by status (PAID/PENDING/CANCELED) for user, 
                    Pageable default: page = 0, size = 10""")
    public List<PaymentDto> searchPayments(@RequestParam(name = "status")
                                           String status,
                                           @PageableDefault(page = 0, size = 10) Pageable pageable,
                                           Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return paymentService.getPaymentsByStatus(user.getId(), status, pageable);
    }

    @ResponseBody
    @PostMapping("/pay")
    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(summary = "Create session",
                description = "Session is created to enable user to use Stripe")
    public PaymentDto createPaymentIntent(@RequestBody @Valid CreatePaymentRequestDto requestDto) {
        return paymentService.createPaymentSession(requestDto);
    }

    @GetMapping("/success")
    @Operation(summary = "Page redirection on successful payment",
                description = """
                        On finishing payment in Stripe user is 
                        redirected to page on successful payment""")
    public String success(@RequestParam String sessionId) {
        paymentService.getSuccessfulPayment(sessionId);
        return "success";
    }

    @GetMapping("/cancel")
    @Operation(summary = "Page redirection on canceled payment",
            description = """
                        On canceling payment in Stripe user is 
                        redirected to page on canceled payment""")
    public String afterCancelPayment(@RequestParam String sessionId) {
        paymentService.getCancelledPayment(sessionId);
        return "cancel";
    }
}
