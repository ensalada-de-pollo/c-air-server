package com.example.cnuairline.seats.dto.request;

import com.example.cnuairline.seats.domain.enums.SeatClass;
import java.time.LocalDate;

public record SeatsFindReqDTO(String arrivalAirport, String departureAirport,
                              LocalDate departureDate, SeatClass seatClass, int noOfSeats) {

}
