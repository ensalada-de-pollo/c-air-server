package com.example.cnuairline.reserve.dto.response;

import com.example.cnuairline.reserve.domain.Reserve;
import com.example.cnuairline.reserve.domain.enums.Status;
import com.example.cnuairline.seats.domain.enums.SeatClass;
import java.time.LocalDateTime;

public record ReserveResDTO(
  Long id,
  String name,
  Long seatId,
  Long ticketId,
  String airline,
  String flightNumber,
  String departureAirport,
  String arrivalAirport,
  LocalDateTime departureDateTime,
  LocalDateTime arrivalDateTime,
  LocalDateTime reserveDateTime,
  SeatClass seatClass,
  int payment,
  Status status
) {

  public static ReserveResDTO toDTO(Reserve reserve) {
    return new ReserveResDTO(
      reserve.getId(),
      reserve.getCustomer().getName(),
      reserve.getSeat().getId(),
      reserve.getTicketId(),
      reserve.getSeat().getAirplane().getAirline(),
      reserve.getSeat().getAirplane().getFlightNumber(),
      reserve.getSeat().getAirplane().getDepartureAirport(),
      reserve.getSeat().getAirplane().getArrivalAirport(),
      reserve.getSeat().getAirplane().getDepartureDateTime(),
      reserve.getSeat().getAirplane().getArrivalDateTime(),
      reserve.getReserveDateTime(),
      reserve.getSeat().getSeatClass(),
      reserve.getPayment(),
      reserve.getStatus()
    );
  }
}
