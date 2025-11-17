package com.example.cnuairline.statistics;

import java.time.LocalDateTime;

public record CustomerSpendingDTO(
  int rank,
  String customerName,
  String flightNumber,
  LocalDateTime departureDateTime,
  String seatClass,
  long latestPayment,
  long totalSpending
) {

}
