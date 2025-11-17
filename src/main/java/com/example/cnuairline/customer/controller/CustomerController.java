package com.example.cnuairline.customer.controller;

import com.example.cnuairline.customer.dto.CustomerResponse;
import com.example.cnuairline.customer.dto.LogInRequest;
import com.example.cnuairline.customer.dto.RegisterRequest;
import com.example.cnuairline.customer.service.CustomerService;
import com.example.cnuairline.jwt.JwtToken;
import com.example.cnuairline.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customers")
public class CustomerController {

  private final JwtTokenProvider jwtTokenProvider;
  private final CustomerService customerService;

  @PostMapping("/register")
  public ResponseEntity<CustomerResponse> register(
    @RequestBody RegisterRequest registerRequest) {
    return ResponseEntity.ok(customerService.register(registerRequest));
  }

  @PostMapping("/login")
  public ResponseEntity<JwtToken> logIn(@RequestBody LogInRequest logInRequest) {
    return ResponseEntity.ok(customerService.logIn(logInRequest));
  }

  @GetMapping()
  public ResponseEntity<CustomerResponse> getCustomerInfo(HttpServletRequest request) {
    String accessToken = request.getHeader("Authorization");
    String cno = jwtTokenProvider.getCnoFromToken(accessToken);
    return ResponseEntity.ok(customerService.getCustomer(cno));
  }

  @GetMapping("/mypage")
  public ResponseEntity<CustomerResponse> getCustomerPage(HttpServletRequest request) {
    String accessToken = request.getHeader("Authorization");
    String cno = jwtTokenProvider.getCnoFromToken(accessToken);

    return ResponseEntity.ok(customerService.getMypage(cno));
  }

}
