package com.example.cnuairline.cancel.service;

import com.example.cnuairline.cancel.domain.Cancel;
import com.example.cnuairline.cancel.dto.CancelRequest;
import com.example.cnuairline.cancel.dto.CancelResponse;
import com.example.cnuairline.cancel.repository.CancelRespository;
import com.example.cnuairline.reserve.domain.Reserve;
import com.example.cnuairline.reserve.domain.enums.Status;
import com.example.cnuairline.reserve.repository.ReserveRepository;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CancelService {

  private final CancelRespository cancelRespository;
  private final ReserveRepository reserveRepository;

  @Transactional
  public CancelResponse cancel(CancelRequest cancelRequest) {
    Long reserveId = cancelRequest.reservationId();
    Reserve reserve = reserveRepository.findById(reserveId)
      .orElseThrow(() -> new RuntimeException("해당 예매 내역을 찾을 수 없습니다."));
    reserve.setStatus(Status.CANCELLED);
    reserveRepository.save(reserve);

    return CancelResponse.toDTO(cancelRespository.save(
        new Cancel(
          reserve,
          reserve.getCustomer(),
          reserve.getPayment() - preview(reserveId) // 결제 금액 - 위약금만큼 환불
        )
      )
    );
  }

  @Transactional(readOnly = true)
  public Integer preview(Long reserveId) {
    Reserve reserve = reserveRepository.findById(reserveId)
      .orElseThrow(() -> new RuntimeException("해당 예매 내역을 찾을 수 없습니다."));
    LocalDate departureDate = reserve.getSeat().getAirplane().getDepartureDateTime().toLocalDate();
    LocalDate now = LocalDate.now();

    long diffDays = ChronoUnit.DAYS.between(now, departureDate);

    if (diffDays >= 15) {
      return 150000;
    } else if (diffDays >= 4) {
      return 180000;
    } else if (diffDays >= 1) {
      return 250000;
    } else {
      return reserve.getPayment();
    }
  }

  @Transactional(readOnly = true)
  public CancelResponse getCancelByReserveId(Long reserveId) {
    return cancelRespository.findCancelByReserveId(reserveId)
      .map(CancelResponse::toDTO)
      .orElseThrow(() -> new RuntimeException("해당 취소 내역을 찾을 수 없습니다."));
  }

  @Transactional(readOnly = true)
  public List<Object> getRefundAmount(Long reserveId) {
    List<Object> list = new ArrayList<>();
    Cancel cancel = cancelRespository.findCancelByReserveId(reserveId)
      .orElseThrow(() -> new RuntimeException("해당 취소 내역을 찾을 수 없습니다."));

    list.add(cancel.getRefund());
    list.add(cancel.getCancelDateTime());
    return list;
  }
}
