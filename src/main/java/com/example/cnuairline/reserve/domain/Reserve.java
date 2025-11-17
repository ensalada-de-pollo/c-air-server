package com.example.cnuairline.reserve.domain;

import com.example.cnuairline.cancel.domain.Cancel;
import com.example.cnuairline.customer.domain.Customer;
import com.example.cnuairline.seats.domain.Seats;
import com.example.cnuairline.reserve.domain.enums.Status;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Reserve {
  @Id
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "seat_id", referencedColumnName = "id")
  private Seats seat;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "cno")
  private Customer customer;

  private Long ticketId;

  private LocalDateTime reserveDateTime;

  private int payment;

  private int noOfSeats;

  @Enumerated(EnumType.STRING)
  private Status status;

  public void setStatus(Status status) {
    this.status = status;
  }
}
