package com.example.cnuairline.jwt;

import com.example.cnuairline.customer.domain.Customer;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtTokenProvider {

  public static final String AUTHORITIES_KEY = "authorities";

  private final SecretKey key;
  private final CustomUserDetailService customUserDetailService;

  public JwtTokenProvider(@Value("${jwt.secret}") String secretKey,
    CustomUserDetailService customUserDetailService) {
    byte[] keyBytes = Base64.getDecoder().decode(secretKey);
    this.key = Keys.hmacShaKeyFor(keyBytes);
    this.customUserDetailService = customUserDetailService;
  }

  public JwtToken generateToken(Authentication authentication, Customer customer) {
    String authorities = customer.getUserRole().name();
    String userId = customer.getCno();

    long now = (new Date()).getTime();

    Date accessTokenExpirationTime = new Date(now + 1000 * 60 * 20); // 20분

    String accessToken = Jwts.builder()
      .subject(authentication.getName())
      .claim("cno", userId)
      .claim(AUTHORITIES_KEY, authorities)
      .expiration(accessTokenExpirationTime)
      .signWith(key)
      .compact();

    Date refreshTokenExpirationTime = new Date(now + 1000 * 60 * 60); // 1시간
    String refreshToken = Jwts.builder()
      .claim("cno", userId)
      .expiration(refreshTokenExpirationTime)
      .signWith(key)
      .compact();

    return JwtToken.builder()
      .cno(userId)
      .accessToken(accessToken)
      .refreshToken(refreshToken)
      .build();
  }

  public Authentication getAuthentication(String token) {
    Claims claims = parseClaims(token);

    if (claims.get(AUTHORITIES_KEY) == null) {
      throw new RuntimeException("권한 인증 정보가 없습니다.");
    }

    Collection<? extends GrantedAuthority> authorities = Arrays.stream(
        claims.get(AUTHORITIES_KEY).toString().split(","))
      .map(SimpleGrantedAuthority::new)
      .toList();

    String username = claims.get("cno", String.class);
    UserDetails userDetails = customUserDetailService.loadUserByUsername(username);

    return new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
      return true;
    } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
      log.info("유효하지 않은 토큰입니다.", e);
    } catch (ExpiredJwtException e) {
      log.info("만료된 토큰입니다.", e);
    } catch (UnsupportedJwtException | IllegalArgumentException e) {
      log.info("지원하지 않는 형식의 토큰입니다.", e);
    } catch (Exception e) {
      log.info("예기치 못한 오류가 발생했습니다.", e);
    }
    return false;
  }

  private Claims parseClaims(String accessToken) {
    try {
      return Jwts.parser().verifyWith(key).build().parseSignedClaims(accessToken).getPayload();
    } catch (ExpiredJwtException e) {
      return e.getClaims();
    }
  }

  public String getCnoFromToken(String accessToken) {
    Claims claims = parseClaims(accessToken);
    return claims.get("cno", String.class);
  }
}
