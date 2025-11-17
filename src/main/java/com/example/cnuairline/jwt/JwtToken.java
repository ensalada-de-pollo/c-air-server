package com.example.cnuairline.jwt;

import lombok.Builder;

@Builder
public record JwtToken(String cno, String accessToken, String refreshToken) {
  
}
