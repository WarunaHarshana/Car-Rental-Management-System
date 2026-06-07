package edu.waruna.carrental.dto;

import lombok.Data;

@Data
public class CarDTO {
    private Long id;
    private String brand;
    private String model;
    private String fuelType;
    private Integer seatingCapacity;
    private Double dailyPrice;
    private String status;
}
