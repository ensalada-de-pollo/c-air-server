package com.example.cnuairline.airplane.repository;

import com.example.cnuairline.airplane.domain.Airplane;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AirplaneRepository extends JpaRepository<Airplane, Long> {
  
}
