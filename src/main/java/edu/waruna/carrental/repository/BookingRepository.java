package edu.waruna.carrental.repository;

import edu.waruna.carrental.dto.CarUtilizationDTO;
import edu.waruna.carrental.dto.CustomerReportDTO;
import edu.waruna.carrental.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByUserId(Long userId);

    List<Booking> findByStatus(String status);

    @Query("""
           SELECT new edu.waruna.carrental.dto.CustomerReportDTO(
               b.user.name,
               b.user.email,
               COUNT(b),
               COALESCE(SUM(b.totalPrice), 0.0)
           )
           FROM Booking b
           GROUP BY b.user.id, b.user.name, b.user.email
           ORDER BY COUNT(b) DESC
           """)
    List<CustomerReportDTO> getCustomerReports();

    @Query("""
           SELECT new edu.waruna.carrental.dto.CarUtilizationDTO(
               c.id,
               CONCAT(c.brand, ' ', c.model),
               c.fuelType,
               COUNT(b),
               COALESCE(SUM(b.totalPrice), 0.0)
           )
           FROM Booking b
           JOIN b.car c
           GROUP BY c.id, c.brand, c.model, c.fuelType
           ORDER BY COUNT(b) DESC
           """)
    List<CarUtilizationDTO> getCarUtilizationReports();
}