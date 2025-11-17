package com.example.cnuairline.seats.repository;

import com.example.cnuairline.seats.domain.Seats;
import com.example.cnuairline.seats.domain.enums.SeatClass;
import com.example.cnuairline.seats.dto.response.SeatAvailabilityDTO;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SeatsRepository extends JpaRepository<Seats, Long> {

  @Query("SELECT new com.example.cnuairline.seats.dto.response.SeatAvailabilityDTO(" +
    "s, " +
    "(s.noOfSeats - COALESCE( " +
    "  (SELECT SUM(r.noOfSeats) " +
    "   FROM Reserve r " +
    "   WHERE r.seat = s AND r.status = 'CONFIRMED'), " +
    "0)) " +
    ") " +
    "FROM Seats s " +
    "JOIN s.airplane a " +
    "WHERE a.departureAirport = :departureAirport " +
    "  AND a.arrivalAirport = :arrivalAirport " +
    "  AND FUNCTION('DATE', a.departureDateTime) = :departureDate " +
    "  AND s.seatClass = :seatClass")
  Page<SeatAvailabilityDTO> findAvailableSeats(
    @Param("departureAirport") String departureAirport,
    @Param("arrivalAirport") String arrivalAirport,
    @Param("departureDate") LocalDate departureDate,
    @Param("seatClass") SeatClass seatClass,
    @Param("noOfSeats") int noOfSeats,
    Pageable pageable
  );

  @Query(value =
    "SELECT s.seat_class AS seatClass, " +
      "COUNT(r.cno) AS totalReservations, " +
      "COUNT(c.cno) AS totalCancellations, " +
      "COALESCE(SUM(r.payment), 0) AS totalRevenue, " +
      "COALESCE(SUM(c.refund), 0) AS totalRefunds, " +
      "COALESCE(SUM(r.payment), 0) - COALESCE(SUM(c.refund), 0) AS netProfit, " +
      "ROUND(COALESCE((COUNT(c.cno) * 100.0) / NULLIF(COUNT(r.cno), 0), 0), 2) AS cancellationRate "
      +
      "FROM seats s " +
      "JOIN airplane a ON s.airplane_id = a.id " +
      "LEFT JOIN reserve r ON s.id = r.seat_id " +
      "LEFT JOIN cancel c ON r.id = c.reserve_id " +
      "WHERE EXTRACT(YEAR FROM r.reserve_date_time) = :year " +
      "  AND EXTRACT(MONTH FROM r.reserve_date_time) = :month " +
      "GROUP BY s.seat_class " +
      "ORDER BY netProfit DESC",
    nativeQuery = true)
  List<Object[]> findSeatClassStatsNative(
    @Param("year") int year,
    @Param("month") int month
  );
}
