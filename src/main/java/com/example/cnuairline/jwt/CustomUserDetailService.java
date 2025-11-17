package com.example.cnuairline.jwt;

import com.example.cnuairline.customer.domain.Customer;
import com.example.cnuairline.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

  private final CustomerRepository customerRepository;

  @Override
  public UserDetails loadUserByUsername(String cno) throws UsernameNotFoundException {
    return customerRepository.findByCno(cno)
      .map(this::createUserDetails)
      .orElseThrow(() -> new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다."));
  }

  private UserDetails createUserDetails(Customer customer) {
    return User.builder()
      .username(customer.getName())
      .password(customer.getPasswd())
      .build();
  }
}
