package com.example.cnuairline.airplane.dto;

import java.time.LocalDateTime;

public record AirplaneRequest(
  String airline,
  String flightNumber,
  LocalDateTime departureDateTime,
  LocalDateTime arrivalDateTime,
  String departureAirport,
  String arrivalAirport
) {

}
