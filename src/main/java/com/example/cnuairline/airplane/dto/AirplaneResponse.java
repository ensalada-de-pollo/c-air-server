package com.example.cnuairline.airplane.dto;

import com.example.cnuairline.airplane.domain.Airplane;
import java.time.LocalDateTime;

public record AirplaneResponse(
  Long id,
  String airline,
  String flightNumber,
  LocalDateTime departureDateTime,
  LocalDateTime arrivalDateTime,
  String departureAirport,
  String arrivalAirport
) {

  public static AirplaneResponse toDTO(Airplane airplane) {
    return new AirplaneResponse(
      airplane.getId(),
      airplane.getAirline(),
      airplane.getFlightNumber(),
      airplane.getDepartureDateTime(),
      airplane.getArrivalDateTime(),
      airplane.getDepartureAirport(),
      airplane.getArrivalAirport()
    );
  }
}
