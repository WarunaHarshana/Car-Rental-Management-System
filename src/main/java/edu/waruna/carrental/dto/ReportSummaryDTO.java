package edu.waruna.carrental.dto;

import lombok.Data;
import java.util.List;

@Data
public class ReportSummaryDTO {

    // Booking stats
    private long totalBookings;
    private long pendingBookings;
    private long approvedBookings;
    private long rejectedBookings;
    private long cancelledBookings;

    // Revenue stats
    private double totalRevenue;
    private long totalPayments;

    // Car fleet stats
    private long totalCars;
    private long availableCars;
    private long rentedCars;
    private long maintenanceCars;

    // Detailed reports
    private List<CustomerReportDTO> customerReports;
    private List<CarUtilizationDTO> carUtilizationReports;
}
