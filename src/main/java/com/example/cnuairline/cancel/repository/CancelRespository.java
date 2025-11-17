package com.example.cnuairline.cancel.repository;

import com.example.cnuairline.cancel.domain.Cancel;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CancelRespository extends JpaRepository<Cancel, Long> {

  @Query("SELECT c FROM Cancel c JOIN c.reserve r WHERE r.id = :id")
  Optional<Cancel> findCancelByReserveId(Long id); // 입력 id를 가진 예매 내역에 대한 취소 내역 생성
}
