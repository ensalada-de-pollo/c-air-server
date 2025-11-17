package com.example.cnuairline.seats.controller;

import com.example.cnuairline.seats.dto.request.PageReqDTO;
import com.example.cnuairline.seats.dto.request.SeatsFindReqDTO;
import com.example.cnuairline.seats.dto.response.SeatAvailabilityDTO;
import com.example.cnuairline.seats.dto.response.SeatsFindResDTO;
import com.example.cnuairline.seats.service.SeatsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/seats")
@Slf4j
public class SeatsController {
  private final SeatsService seatsService;

  @GetMapping
  public Page<SeatAvailabilityDTO> findAvailableSeats(SeatsFindReqDTO seatsFindReqDTO, PageReqDTO pageReqDTO) {
    return seatsService.findAvailableSeats(seatsFindReqDTO, pageReqDTO);
  }

  @GetMapping("/all")
  public ResponseEntity<List<SeatsFindResDTO>> findAllSeats() {
    return ResponseEntity.ok(seatsService.findAllSeats());
  }
}
