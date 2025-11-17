package com.example.cnuairline.seats.dto.request;

import org.springframework.data.domain.Sort;

public record PageReqDTO(int page, int size, Sort.Direction direction, String criteria) {
  
}
