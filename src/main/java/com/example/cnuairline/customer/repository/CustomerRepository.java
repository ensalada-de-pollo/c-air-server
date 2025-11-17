package com.example.cnuairline.customer.repository;

import com.example.cnuairline.customer.domain.Customer;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, String> {

  boolean existsByEmail(String email);

  Optional<Customer> findByCno(String cno);
}
