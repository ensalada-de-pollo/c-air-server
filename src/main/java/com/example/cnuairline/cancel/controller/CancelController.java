package com.example.cnuairline.cancel.controller;

import com.example.cnuairline.cancel.dto.CancelRequest;
import com.example.cnuairline.cancel.dto.CancelResponse;
import com.example.cnuairline.cancel.service.CancelService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cancels")
@RequiredArgsConstructor
public class CancelController {

  private final CancelService cancelService;

  @PostMapping
  public ResponseEntity<CancelResponse> cancel(@RequestBody CancelRequest cancelRequest) {
    return ResponseEntity.ok(cancelService.cancel(cancelRequest));
  }

  @GetMapping("/preview/{reserveId}")
  public ResponseEntity<Integer> preview(@PathVariable Long reserveId) {
    return ResponseEntity.ok(cancelService.preview(reserveId));
  }

  @GetMapping("/{reserveId}")
  public ResponseEntity<CancelResponse> findByReserveId(@PathVariable Long reserveId) {
    return ResponseEntity.ok(cancelService.getCancelByReserveId(reserveId));
  }

  @GetMapping("/{reserveId}/refund-info")
  public ResponseEntity<List<Object>> getRefundInfo(@PathVariable Long reserveId) {
    return ResponseEntity.ok(cancelService.getRefundAmount(reserveId));
  }
}
