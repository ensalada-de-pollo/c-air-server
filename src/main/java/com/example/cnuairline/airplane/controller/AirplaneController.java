package com.example.cnuairline.airplane.controller;

import com.example.cnuairline.airplane.dto.AirplaneRequest;
import com.example.cnuairline.airplane.dto.AirplaneResponse;
import com.example.cnuairline.airplane.service.AirplaneService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/airplanes")
public class AirplaneController {

  private final AirplaneService airplaneService;

  @PostMapping
  public ResponseEntity<AirplaneResponse> save(@RequestBody AirplaneRequest airplaneRequest) {
    return ResponseEntity.ok(airplaneService.save(airplaneRequest));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    airplaneService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
