package com.example.cnuairline.airplane.service;

import com.example.cnuairline.airplane.domain.Airplane;
import com.example.cnuairline.airplane.dto.AirplaneRequest;
import com.example.cnuairline.airplane.dto.AirplaneResponse;
import com.example.cnuairline.airplane.repository.AirplaneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AirplaneService {

  private final AirplaneRepository airplaneRepository;

  @Transactional
  public AirplaneResponse save(AirplaneRequest airplaneRequest) {
    Airplane airplane = new Airplane(
      airplaneRequest.airline(),
      airplaneRequest.flightNumber(),
      airplaneRequest.departureDateTime(),
      airplaneRequest.arrivalDateTime(),
      airplaneRequest.departureAirport(),
      airplaneRequest.arrivalAirport()
    );

    return AirplaneResponse.toDTO(airplaneRepository.save(airplane));
  }

  @Transactional
  public void delete(Long id) {
    airplaneRepository.deleteById(id);
  }
}
