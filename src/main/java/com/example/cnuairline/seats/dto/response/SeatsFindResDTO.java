package com.example.cnuairline.seats.dto.response;

import com.example.cnuairline.seats.domain.Seats;
import com.example.cnuairline.seats.domain.enums.SeatClass;
import java.time.LocalDateTime;

public record SeatsFindResDTO(
  Long id,
  String airline,
  String flightNumber,
  String arrivalAirport,
  String departureAirport,
  LocalDateTime departureDateTime,
  LocalDateTime arrivalDateTime,
  int price,
  SeatClass seatClass,
  int noOfSeats
) {

  public static SeatsFindResDTO toDTO(Seats seats) {
    return new SeatsFindResDTO(
      seats.getId(),
      seats.getAirplane().getAirline(),
      seats.getAirplane().getFlightNumber(),
      seats.getAirplane().getArrivalAirport(),
      seats.getAirplane().getDepartureAirport(),
      seats.getAirplane().getDepartureDateTime(),
      seats.getAirplane().getArrivalDateTime(),
      seats.getPrice(),
      seats.getSeatClass(),
      seats.getNoOfSeats()
    );
  }
}
