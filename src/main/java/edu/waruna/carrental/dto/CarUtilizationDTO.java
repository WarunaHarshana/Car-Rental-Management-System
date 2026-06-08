package edu.waruna.carrental.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CarUtilizationDTO {
    private Long carId;
    private String carName;
    private String fuelType;
    private long bookingCount;
    private double totalRevenue;
}
