package com.example.cnuairline.airplane.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Airplane {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String airline;

  private String flightNumber;

  private LocalDateTime departureDateTime;

  private LocalDateTime arrivalDateTime;

  private String departureAirport;

  private String arrivalAirport;

  public Airplane(String airline, String flightNumber, LocalDateTime departureDateTime,
    LocalDateTime arrivalDateTime, String departureAirport, String arrivalAirport) {
    this.airline = airline;
    this.flightNumber = flightNumber;
    this.departureDateTime = departureDateTime;
    this.arrivalDateTime = arrivalDateTime;
    this.departureAirport = departureAirport;
    this.arrivalAirport = arrivalAirport;
  }
}
