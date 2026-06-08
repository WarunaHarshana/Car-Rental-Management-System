package edu.waruna.carrental.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomerReportDTO {
    private String customerName;
    private String customerEmail;
    private long bookingCount;
    private double totalSpent;
}
