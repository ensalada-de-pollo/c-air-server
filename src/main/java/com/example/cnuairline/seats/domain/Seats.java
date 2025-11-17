package com.example.cnuairline.seats.domain;

import com.example.cnuairline.airplane.domain.Airplane;
import com.example.cnuairline.seats.domain.enums.SeatClass;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Seats {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "airplane_id", referencedColumnName = "id")
  private Airplane airplane;

  @Enumerated(EnumType.STRING)
  private SeatClass seatClass;

  private int price;

  private int noOfSeats;

}
