package com.example.cnuairline.reserve.controller;

import com.example.cnuairline.jwt.JwtTokenProvider;
import com.example.cnuairline.reserve.domain.enums.Status;
import com.example.cnuairline.reserve.dto.request.FindReserveReqDTO;
import com.example.cnuairline.reserve.dto.request.ReserveReqDTO;
import com.example.cnuairline.reserve.dto.response.ReserveResDTO;
import com.example.cnuairline.reserve.service.ReserveService;
import com.example.cnuairline.seats.dto.request.PageReqDTO;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reservation")
@RequiredArgsConstructor
@Slf4j
public class ReserveController {

  private final ReserveService reserveService;
  private final JwtTokenProvider jwtTokenProvider;

  @PostMapping
  public ResponseEntity<List<ReserveResDTO>> create(HttpServletRequest request,
    @RequestBody ReserveReqDTO reserveReqDTO) {
    String accessToken = request.getHeader("Authorization");
    String cno = jwtTokenProvider.getCnoFromToken(accessToken);

    return ResponseEntity.ok(reserveService.create(cno, reserveReqDTO));
  }

  @GetMapping("/mypage")
  public ResponseEntity<Page<ReserveResDTO>> findByCno(
    HttpServletRequest request,
    FindReserveReqDTO findReserveReqDTO,
    PageReqDTO pageReqDTO
  ) {
    String accessToken = request.getHeader("Authorization");
    String cno = jwtTokenProvider.getCnoFromToken(accessToken);

    return ResponseEntity.ok(reserveService.findByCno(cno, findReserveReqDTO, pageReqDTO));
  }

  @GetMapping("/status/{id}")
  public ResponseEntity<Status> getStatus(@PathVariable Long id) {
    return ResponseEntity.ok(reserveService.findStatusById(id));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ReserveResDTO> findById(@PathVariable Long id) {
    return ResponseEntity.ok(reserveService.findById(id));
  }
}
