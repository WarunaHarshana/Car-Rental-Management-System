package edu.waruna.carrental.controller;

import edu.waruna.carrental.dto.PaymentDTO;
import edu.waruna.carrental.entity.Payment;
import edu.waruna.carrental.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    // Checkout endpoint: creates a Payment and marks booking PAID
    @PostMapping("/checkout/{bookingId}")
    public ResponseEntity<?> checkout(@PathVariable Long bookingId, @RequestBody Map<String, Object> body) {
        Double amount = null;
        String method = "UNKNOWN";
        if (body != null) {
            if (body.get("amount") instanceof Number) {
                amount = ((Number) body.get("amount")).doubleValue();
            }
            if (body.get("method") != null) {
                method = body.get("method").toString();
            }
        }

        if (amount == null) {
            return ResponseEntity.badRequest().body("Missing amount");
        }

        try {
            Payment payment = paymentService.createPaymentForBooking(bookingId, amount, method);
            PaymentDTO dto = new PaymentDTO();
            dto.setId(payment.getId());
            dto.setAmount(payment.getAmount());
            dto.setMethod(payment.getMethod());
            dto.setStatus(payment.getStatus());
            dto.setCreatedAt(payment.getCreatedAt());
            if (payment.getBooking() != null) {
                dto.setBookingId(payment.getBooking().getId());
            }
            return ResponseEntity.ok(dto);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(404).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(500).build();
        }
    }

    // All payments
    @GetMapping
    public List<PaymentDTO> getAllPayments() {
        return paymentService.getAllPayments().stream().map(p -> {
            PaymentDTO dto = new PaymentDTO();
            dto.setId(p.getId());
            dto.setAmount(p.getAmount());
            dto.setMethod(p.getMethod());
            dto.setStatus(p.getStatus());
            dto.setCreatedAt(p.getCreatedAt());
            if (p.getBooking() != null) {
                dto.setBookingId(p.getBooking().getId());
            }
            return dto;
        }).collect(Collectors.toList());
    }

    // Payment history for one booking
    @GetMapping("/booking/{bookingId}")
    public List<PaymentDTO> getByBooking(@PathVariable Long bookingId) {
        return paymentService.getPaymentsForBooking(bookingId).stream().map(p -> {
            PaymentDTO dto = new PaymentDTO();
            dto.setId(p.getId());
            dto.setAmount(p.getAmount());
            dto.setMethod(p.getMethod());
            dto.setStatus(p.getStatus());
            dto.setCreatedAt(p.getCreatedAt());
            if (p.getBooking() != null) {
                dto.setBookingId(p.getBooking().getId());
            }
            return dto;
        }).collect(Collectors.toList());
    }
}
