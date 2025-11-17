package com.example.cnuairline.customer.domain;

import com.example.cnuairline.customer.domain.enums.UserRole;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Customer {

  @Id
  private String cno;

  private String name;

  private String passwd;

  private String email;

  private String passportNumber;

  @Enumerated(EnumType.STRING)
  private UserRole userRole;
}
