package com.example.cnuairline.reserve.repository;

import com.example.cnuairline.reserve.domain.Reserve;
import com.example.cnuairline.reserve.domain.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReserveRepository extends JpaRepository<Reserve, Long> {
  @Query("SELECT r FROM Reserve r " +
        "JOIN r.customer c " +
        "JOIN r.seat s " +
        "JOIN s.airplane a " +
        "WHERE c.cno = :cno " +
        "AND (:status IS NULL OR r.status = :status) " +
        "AND ((:startDate IS NULL OR r.reserveDateTime >= :startDate) " +
        "     AND (:endDate IS NULL OR r.reserveDateTime <= :endDate)) " +
        "AND (:before IS NULL OR a.departureDateTime >= :before) " + // 출발 전
        "ORDER BY r.reserveDateTime DESC")
  Page<Reserve> findByCnoWithFilters(
    @Param("cno") String cno,
    @Param("status") Status status,
    @Param("startDate") LocalDateTime startDate,   // 시작일
    @Param("endDate") LocalDateTime endDate,       // 종료일
    @Param("before") LocalDateTime before,
    Pageable pageable
  );


  @Query("SELECT status FROM Reserve WHERE id = :id")
  Optional<Status> findStatusById(Long id);

  @Query(value = """
    WITH CustomerLatestFlight AS (
      SELECT
        c.cno,
        c.name AS customer_name,
        a.flight_number,
        a.departure_date_time,
        s.seat_class,
        r.payment,
        r.reserve_date_time,
        ROW_NUMBER() OVER (
          PARTITION BY r.cno
          ORDER BY r.reserve_date_time DESC
        ) AS rn
      FROM reserve r
      JOIN customer c ON r.cno = c.cno
      JOIN seats s ON r.seat_id = s.id
      JOIN airplane a ON s.airplane_id = a.id
      WHERE EXTRACT(YEAR FROM r.reserve_date_time) = :year
        AND EXTRACT(MONTH FROM r.reserve_date_time) = :month
    ),
    CustomerCumulative AS (
      SELECT
        c.cno,
        SUM(
          CASE 
            WHEN r.status = 'CANCELLED' THEN r.payment - COALESCE(ca.refund, 0)
            ELSE r.payment
          END
        ) AS total_spending
      FROM reserve r
      LEFT JOIN cancel ca ON r.id = ca.reserve_id
      JOIN customer c ON r.cno = c.cno
      WHERE EXTRACT(YEAR FROM r.reserve_date_time) = :year
        AND EXTRACT(MONTH FROM r.reserve_date_time) = :month
      GROUP BY c.cno
    ),
    CustomerRanks AS (
      SELECT
        c.cno,
        c.name AS customer_name,
        cc.total_spending,
        DENSE_RANK() OVER (ORDER BY cc.total_spending DESC) AS overall_rank
      FROM customer c
      JOIN CustomerCumulative cc ON c.cno = cc.cno
    )
    SELECT
      r.overall_rank,
      lf.customer_name,
      lf.flight_number,
      lf.departure_date_time,
      lf.seat_class,
      lf.payment AS latest_payment,
      cc.total_spending
    FROM CustomerRanks r
    JOIN CustomerLatestFlight lf ON r.cno = lf.cno
    JOIN CustomerCumulative cc ON r.cno = cc.cno
    WHERE lf.rn = 1
    ORDER BY r.overall_rank;
  """, nativeQuery = true)
  List<Object[]> findCustomerSpendingStats(
          @Param("year") int year,
          @Param("month") int month
  );

}
