package com.example.cnuairline.seats.dto.response;

import com.example.cnuairline.seats.domain.Seats;
import lombok.Getter;

@Getter
public class SeatAvailabilityDTO {

  private final SeatsFindResDTO seatsFindResDTO;
  private final Long availableSeats;

  public SeatAvailabilityDTO(Seats seats, Long availableSeats) {
    this.seatsFindResDTO = SeatsFindResDTO.toDTO(seats);
    this.availableSeats = availableSeats;
  }
}
