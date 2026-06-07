package edu.waruna.carrental.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class BookingDTO {
    private Long id;
    private String customerName;
    private String contactNumber;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double totalPrice;
    private String status;
    private String paymentStatus;
    private CarDTO car;
    private UserDTO user;
}
