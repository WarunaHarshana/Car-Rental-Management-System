package edu.waruna.carrental.dto;

import edu.waruna.carrental.entity.Payment;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
public class PaymentResponse {

    private Long id;
    private Long bookingId;
    private Double amount;
    private String method;
    private String status;
    private String createdAt;

    public static PaymentResponse from(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getBooking() != null ? payment.getBooking().getId() : null,
                payment.getAmount(),
                payment.getMethod(),
                payment.getStatus(),
                payment.getCreatedAt() != null
                        ? payment.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                        : null
        );
    }
}