package edu.waruna.carrental.controller;

import edu.waruna.carrental.dto.ReportSummaryDTO;
import edu.waruna.carrental.entity.Booking;
import edu.waruna.carrental.entity.Car;
import edu.waruna.carrental.entity.Payment;
import edu.waruna.carrental.repository.BookingRepository;
import edu.waruna.carrental.repository.CarRepository;
import edu.waruna.carrental.repository.PaymentRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

        private final BookingRepository bookingRepository;
        private final CarRepository carRepository;
        private final PaymentRepository paymentRepository;

        public ReportController(
                        BookingRepository bookingRepository,
                        CarRepository carRepository,
                        PaymentRepository paymentRepository) {
                this.bookingRepository = bookingRepository;
                this.carRepository = carRepository;
                this.paymentRepository = paymentRepository;
        }

        @GetMapping("/summary")
        public ReportSummaryDTO getSummary() {
                List<Booking> bookings = bookingRepository.findAll();
                List<Car> cars = carRepository.findAll();
                List<Payment> payments = paymentRepository.findAll();

                ReportSummaryDTO report = new ReportSummaryDTO();

                // Booking stats
                report.setTotalBookings(bookings.size());
                report.setPendingBookings(
                                bookings.stream().filter(b -> "PENDING".equalsIgnoreCase(b.getStatus())).count());
                report.setApprovedBookings(
                                bookings.stream().filter(b -> "APPROVED".equalsIgnoreCase(b.getStatus())).count());
                report.setRejectedBookings(
                                bookings.stream().filter(b -> "REJECTED".equalsIgnoreCase(b.getStatus())).count());
                report.setCancelledBookings(
                                bookings.stream().filter(b -> "CANCELLED".equalsIgnoreCase(b.getStatus())).count());

                // Revenue stats
                report.setTotalPayments(payments.size());
                report.setTotalRevenue(
                                payments.stream()
                                                .filter(p -> "SUCCESS".equalsIgnoreCase(p.getStatus())
                                                                || "PAID".equalsIgnoreCase(p.getStatus()))
                                                .mapToDouble(Payment::getAmount)
                                                .sum());

                // Car fleet stats
                report.setTotalCars(cars.size());
                report.setAvailableCars(
                                cars.stream().filter(c -> "AVAILABLE".equalsIgnoreCase(c.getStatus())).count());
                report.setRentedCars(
                                cars.stream().filter(c -> "RENTED".equalsIgnoreCase(c.getStatus())).count());
                report.setMaintenanceCars(
                                cars.stream().filter(c -> "MAINTENANCE".equalsIgnoreCase(c.getStatus())).count());

                // Detailed reports
                report.setCustomerReports(bookingRepository.getCustomerReports());
                report.setCarUtilizationReports(bookingRepository.getCarUtilizationReports());

                return report;
        }
}
