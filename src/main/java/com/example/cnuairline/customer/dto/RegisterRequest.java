package com.example.cnuairline.customer.dto;

import com.example.cnuairline.customer.domain.enums.UserRole;

public record RegisterRequest(
  String cno,
  String name,
  String passwd,
  String email,
  String passportNumber,
  UserRole userRole
) {

}