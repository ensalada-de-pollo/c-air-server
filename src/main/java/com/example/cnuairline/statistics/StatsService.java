package com.example.cnuairline.statistics;

import com.example.cnuairline.reserve.repository.ReserveRepository;
import com.example.cnuairline.seats.domain.enums.SeatClass;
import com.example.cnuairline.seats.repository.SeatsRepository;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatsService {

  private final SeatsRepository seatsRepository;
  private final ReserveRepository reserveRepository;

  public List<SeatClassStatsDTO> getSeatClassStats(int year, int month) {
    List<Object[]> results = seatsRepository.findSeatClassStatsNative(year, month);
    return results.stream().map(row -> {
      SeatClass seatClass = row[0] != null ?
        (row[0] instanceof SeatClass ? (SeatClass) row[0] : SeatClass.valueOf(row[0].toString()))
        : null;

      long totalReservations = row[1] != null ? ((Number) row[1]).longValue() : 0L;
      long totalCancellations = row[2] != null ? ((Number) row[2]).longValue() : 0L;
      long totalRevenue = row[3] != null ? ((Number) row[3]).longValue() : 0L;
      long totalRefunds = row[4] != null ? ((Number) row[4]).longValue() : 0L;
      long netProfit = row[5] != null ? ((Number) row[5]).longValue() : 0L;
      double cancellationRate = row[6] != null ? ((Number) row[6]).doubleValue() : 0.0;

      return new SeatClassStatsDTO(
        seatClass,
        totalReservations,
        totalCancellations,
        totalRevenue,
        totalRefunds,
        netProfit,
        cancellationRate
      );
    }).collect(Collectors.toList());
  }

  public List<CustomerSpendingDTO> getCustomerSpendingStats(int year, int month) {
    List<Object[]> results = reserveRepository.findCustomerSpendingStats(year, month);
    return results.stream().map(row -> new CustomerSpendingDTO(
      ((Number) row[0]).intValue(),      // rank
      (String) row[1],                   // customerName
      (String) row[2],                   // flightNumber
      ((Timestamp) row[3]).toLocalDateTime(), // departureDateTime
      (String) row[4],                   // seatClass
      ((Number) row[5]).longValue(),     // latestPayment
      ((Number) row[6]).longValue()      // totalSpending
    )).collect(Collectors.toList());
  }

}
