package com.example.cnuairline.reserve.service;

import com.example.cnuairline.common.PdfFormUtil;
import com.example.cnuairline.customer.domain.Customer;
import com.example.cnuairline.customer.repository.CustomerRepository;
import com.example.cnuairline.email.EmailService;
import com.example.cnuairline.reserve.domain.Reserve;
import com.example.cnuairline.reserve.domain.enums.Status;
import com.example.cnuairline.reserve.dto.request.FindReserveReqDTO;
import com.example.cnuairline.reserve.dto.request.ReserveReqDTO;
import com.example.cnuairline.reserve.dto.response.ReserveResDTO;
import com.example.cnuairline.reserve.repository.ReserveRepository;
import com.example.cnuairline.seats.domain.Seats;
import com.example.cnuairline.seats.dto.request.PageReqDTO;
import com.example.cnuairline.seats.repository.SeatsRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReserveService {

  private final ReserveRepository reserveRepository;
  private final CustomerRepository customerRepository;
  private final SeatsRepository seatsRepository;
  private final EmailService emailService;

  @Transactional
  public List<ReserveResDTO> create(String cno, ReserveReqDTO reserveReqDTO) {
    Customer customer = customerRepository.findByCno(cno)
      .orElseThrow(() -> new UsernameNotFoundException("Customer not found"));

    Seats seat = seatsRepository.findById(reserveReqDTO.seatId())
      .orElseThrow(() -> new UsernameNotFoundException("Seat not found"));

    List<ReserveResDTO> list = new ArrayList<>();

    for (int i = 0; i < reserveReqDTO.noOfSeats(); i++) {
      Reserve reserve = reserveRepository.save(new Reserve(
          (long) (Math.random() * 899999) + 100000,
          seat,
          customer,
          (long) (Math.random() * 899999) + 100000,
          LocalDateTime.now(),
          reserveReqDTO.price(),
          reserveReqDTO.noOfSeats(),
          Status.CONFIRMED
        )
      );

      try {
        String fileName = PdfFormUtil.fillPdfForm(reserve);
        emailService.sendPdfWithEmail(
          reserve.getCustomer().getEmail(),
          "예매 내역에 대한 e-ticket입니다.",
          "예매하신 내역에 대한 e-ticket을 pdf 파일로 첨부합니다.",
          "src/main/pdf/result/" + fileName
        );
      } catch (Exception e) {
        e.printStackTrace();
      }

      list.add(ReserveResDTO.toDTO(reserve));
    }

    return list;
  }

  @Transactional(readOnly = true)
  public Page<ReserveResDTO> findByCno(
    String cno,
    FindReserveReqDTO findReserveReqDTO,
    PageReqDTO pageReqDTO
  ) {
    Pageable pageable = PageRequest.of(
      pageReqDTO.page(),
      pageReqDTO.size(),
      Sort.by(pageReqDTO.direction(), pageReqDTO.criteria())
    );

    // null-safe 처리
    Status status = null;
    Integer flag = null;
    LocalDate startDate = null;
    LocalDate endDate = null;

    if (findReserveReqDTO != null) {
      status = findReserveReqDTO.status();
      flag = findReserveReqDTO.flag();
      startDate = findReserveReqDTO.start();
      endDate = findReserveReqDTO.end();
    }

    // 날짜 필터링
    LocalDateTime startDateTime = null;
    LocalDateTime endDateTime = null;
    LocalDateTime before = null;

    if (flag != null && flag == 1) {
      before = LocalDateTime.now();
    } else {
      // 날짜 범위 조회
      startDateTime = convertToStartOfDay(startDate);
      endDateTime = convertToEndOfDay(endDate);
    }

    log.info("findReserveReqDTO={}", findReserveReqDTO);
    log.info("status: {}, startDateTime: {}, endDateTime: {}", status, startDateTime, endDateTime);

    return reserveRepository.findByCnoWithFilters(
      cno,
      status,
      startDateTime,
      endDateTime,
      before,
      pageable
    ).map(ReserveResDTO::toDTO);
  }

  // 날짜 변환 메서드
  private LocalDateTime convertToStartOfDay(LocalDate date) {
    return date != null ? date.atStartOfDay() : null;
  }

  private LocalDateTime convertToEndOfDay(LocalDate date) {
    return date != null ? date.atTime(LocalTime.MAX) : null;
  }

  @Transactional(readOnly = true)
  public ReserveResDTO findById(@PathVariable Long id) {
    return reserveRepository.findById(id)
      .map(ReserveResDTO::toDTO)
      .orElseThrow(() -> new UsernameNotFoundException("해당 예매 내역을 찾을 수 없습니다."));
  }

  @Transactional(readOnly = true)
  public Status findStatusById(@PathVariable Long id) {
    return reserveRepository.findStatusById(id)
      .orElseThrow(() -> new RuntimeException("해당 예매 내역을 찾을 수 없습니다."));
  }
}