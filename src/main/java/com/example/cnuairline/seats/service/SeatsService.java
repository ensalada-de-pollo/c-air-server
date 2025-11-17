package com.example.cnuairline.seats.service;

import com.example.cnuairline.seats.dto.request.PageReqDTO;
import com.example.cnuairline.seats.dto.request.SeatsFindReqDTO;
import com.example.cnuairline.seats.dto.response.SeatAvailabilityDTO;
import com.example.cnuairline.seats.dto.response.SeatsFindResDTO;
import com.example.cnuairline.seats.repository.SeatsRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SeatsService {

  private final SeatsRepository seatsRepository;

  @Transactional(readOnly = true)
  public Page<SeatAvailabilityDTO> findAvailableSeats(SeatsFindReqDTO seatsFindReqDTO,
    PageReqDTO pageReqDTO) {
    Pageable pageable = PageRequest.of(pageReqDTO.page(), pageReqDTO.size(),
      Sort.by(pageReqDTO.direction(), pageReqDTO.criteria()));

    return seatsRepository.findAvailableSeats(
      seatsFindReqDTO.departureAirport(),
      seatsFindReqDTO.arrivalAirport(),
      seatsFindReqDTO.departureDate(),
      seatsFindReqDTO.seatClass(),
      seatsFindReqDTO.noOfSeats(),
      pageable);
  }

  @Transactional(readOnly = true)
  public List<SeatsFindResDTO> findAllSeats() {
    return seatsRepository.findAll().stream()
      .map(SeatsFindResDTO::toDTO)
      .toList();
  }
}
