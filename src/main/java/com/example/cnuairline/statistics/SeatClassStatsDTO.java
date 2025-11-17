package com.example.cnuairline.statistics;

import com.example.cnuairline.seats.domain.enums.SeatClass;

public record SeatClassStatsDTO(
  SeatClass seatClass,
  long totalReservations,
  long totalCancellations,
  long totalRevenue,
  long totalRefunds,
  long netProfit,
  double cancellationRate
) {

}
