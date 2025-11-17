package com.example.cnuairline.cancel.dto;

import com.example.cnuairline.cancel.domain.Cancel;
import java.time.LocalDateTime;

public record CancelResponse(
  Long id,
  String airline,
  String departureAirport,
  String arrivalAirport,
  LocalDateTime departureDateTime,
  LocalDateTime arrivalDateTime,
  LocalDateTime cancelDateTime,
  int refund
) {

  public static CancelResponse toDTO(Cancel cancel) {
    return new CancelResponse(
      cancel.getId(),
      cancel.getReserve().getSeat().getAirplane().getAirline(),
      cancel.getReserve().getSeat().getAirplane().getDepartureAirport(),
      cancel.getReserve().getSeat().getAirplane().getArrivalAirport(),
      cancel.getReserve().getSeat().getAirplane().getDepartureDateTime(),
      cancel.getReserve().getSeat().getAirplane().getArrivalDateTime(),
      cancel.getCancelDateTime(),
      cancel.getRefund()
    );
  }
}
