package edu.waruna.carrental.controller;

import edu.waruna.carrental.entity.Booking;
import edu.waruna.carrental.entity.Car;
import edu.waruna.carrental.repository.BookingRepository;
import edu.waruna.carrental.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "*")
public class BookingController {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private CarRepository carRepository;

    //  Place a booking
    @PostMapping
    public ResponseEntity<?> placeBooking(@RequestBody Booking booking) {

        Optional<Car> carOpt = carRepository.findById(booking.getCar().getId());

        if (carOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: Car not found!");
        }

        Car car = carOpt.get();
        if (!"AVAILABLE".equals(car.getStatus())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: This car is currently rented or under maintenance!");
        }

        booking.setStatus("PENDING");
        booking.setPaymentStatus("PENDING");

        Booking savedBooking = bookingRepository.save(booking);

        return ResponseEntity.ok(savedBooking);
    }

    // get all bookings
    @GetMapping
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    // get customer booking history
    @GetMapping("/user/{userId}")
    public List<Booking> getBookingsByUserId(@PathVariable Long userId) {
        return bookingRepository.findByUserId(userId);
    }

    // update booking status
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateBookingStatus(@PathVariable Long id, @RequestParam String status) {
        Optional<Booking> bookingOpt = bookingRepository.findById(id);
        if (bookingOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: Booking record not found!");
        }

        Booking booking = bookingOpt.get();
        booking.setStatus(status.toUpperCase());

        // If an admin approves the booking automatically flip the car status to RENTED
        if ("APPROVED".equalsIgnoreCase(status)) {
            Car car = booking.getCar();
            car.setStatus("RENTED");
            carRepository.save(car);
        }
        // If the booking is completed or canceled release the car back to AVAILABLE
        else if ("CANCELLED".equalsIgnoreCase(status) || "COMPLETED".equalsIgnoreCase(status)) {
            Car car = booking.getCar();
            car.setStatus("AVAILABLE");
            carRepository.save(car);
        }

        Booking updatedBooking = bookingRepository.save(booking);
        return ResponseEntity.ok(updatedBooking);
    }
}