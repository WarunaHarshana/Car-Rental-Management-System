package edu.waruna.carrental.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PaymentDTO {
    private Long id;
    private Double amount;
    private String method;
    private String status;
    private LocalDateTime createdAt;
    private Long bookingId;
}
