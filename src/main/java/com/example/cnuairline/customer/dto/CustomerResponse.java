package com.example.cnuairline.customer.dto;

import com.example.cnuairline.customer.domain.Customer;
import com.example.cnuairline.customer.domain.enums.UserRole;

public record CustomerResponse(
  String cno,
  String name,
  String email,
  String passportNumber,
  UserRole userRole)
{

  public static CustomerResponse toDTO(Customer customer) {
    return new CustomerResponse(
      customer.getCno(),
      customer.getName(),
      customer.getEmail(),
      customer.getPassportNumber(),
      customer.getUserRole()
    );
  }
}
