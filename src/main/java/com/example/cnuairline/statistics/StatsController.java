package com.example.cnuairline.statistics;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
public class StatsController {

  private final StatsService statsService;

  @GetMapping("/seat-class")
  public List<SeatClassStatsDTO> getSeatClassStats(
    @RequestParam int year,
    @RequestParam int month
  ) {
    return statsService.getSeatClassStats(year, month);
  }

  @GetMapping("/customer-spending")
  public List<CustomerSpendingDTO> getCustomerSpendingStats(
    @RequestParam int year,
    @RequestParam int month
  ) {
    return statsService.getCustomerSpendingStats(year, month);
  }
}
