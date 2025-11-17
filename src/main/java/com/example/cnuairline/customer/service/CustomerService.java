package com.example.cnuairline.customer.service;

import com.example.cnuairline.customer.domain.Customer;
import com.example.cnuairline.customer.domain.enums.UserRole;
import com.example.cnuairline.customer.dto.CustomerResponse;
import com.example.cnuairline.customer.dto.LogInRequest;
import com.example.cnuairline.customer.dto.RegisterRequest;
import com.example.cnuairline.customer.repository.CustomerRepository;
import com.example.cnuairline.jwt.JwtToken;
import com.example.cnuairline.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomerService {

  private final CustomerRepository customerRepository;
  private final AuthenticationManagerBuilder authenticationManagerBuilder;
  private final JwtTokenProvider jwtTokenProvider;
  private final PasswordEncoder passwordEncoder;

  @Transactional
  public JwtToken logIn(LogInRequest logInRequest) {
    String cno = logInRequest.cno();
    String passwd = logInRequest.passwd();

    UsernamePasswordAuthenticationToken authenticationToken =
      new UsernamePasswordAuthenticationToken(cno, passwd);

    Authentication authentication = authenticationManagerBuilder.getObject()
      .authenticate(authenticationToken);

    Customer customer = customerRepository.findByCno(cno)
      .orElseThrow(() -> new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다."));

    return jwtTokenProvider.generateToken(authentication, customer);
  }

  @Transactional
  public CustomerResponse register(RegisterRequest registerRequest) {
    String cno = registerRequest.cno();

    if (cno == null) {
      cno = "c" + (int) (Math.random() * 899999) + 100000;
    }

    String name = registerRequest.name();
    String passwd = registerRequest.passwd();
    String email = registerRequest.email();
    String passportNumber = registerRequest.passportNumber();
    UserRole userRole = registerRequest.userRole();

    if (customerRepository.existsByEmail(email)) {
      throw new RuntimeException("이미 가입된 사용자입니다.");
    }

    return CustomerResponse.toDTO(
      customerRepository.save(new Customer(
          cno,
          name,
          passwordEncoder.encode(passwd),
          email,
          passportNumber,
          userRole
        )
      )
    );
  }

  @Transactional(readOnly = true)
  public CustomerResponse getCustomer(String cno) {
    return customerRepository.findByCno(cno)
      .map(CustomerResponse::toDTO)
      .orElseThrow(() -> new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다."));
  }

  @Transactional(readOnly = true)
  public CustomerResponse getMypage(String cno) {
    return customerRepository.findByCno(cno).map(CustomerResponse::toDTO)
      .orElseThrow(() -> new RuntimeException("해당 회원을 찾을 수 없습니다."));
  }
}
