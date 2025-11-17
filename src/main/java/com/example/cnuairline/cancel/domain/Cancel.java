package com.example.cnuairline.cancel.domain;

import com.example.cnuairline.customer.domain.Customer;
import com.example.cnuairline.reserve.domain.Reserve;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cancel {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne
  @JoinColumn(name = "reserve_id")
  private Reserve reserve;

  @ManyToOne
  @JoinColumn(name = "cno")
  private Customer customer;

  @CreatedDate
  private LocalDateTime cancelDateTime;

  private int refund;

  public Cancel(Reserve reserve, Customer customer, int refund) {
    this.reserve = reserve;
    this.customer = customer;
    this.refund = refund;
  }
}
