package com.sist.web.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JWTAuthenticationProvider {
	private final String SECRET="one-secret-key-two-secret-key-three-secret-key"; // 키 길이랑 내용은 자유롭게
	public String createToken(String username,String role)
	{
		// Payload => {sub:"admin",role:"ROLE_ADMIN"}
		// 토큰 만드는 중(생성)
		return Jwts.builder()
				// 사용자 아이디
				.setSubject(username)
				// 사용자 권한
				.claim("role", role)
				// JWT 토큰 발급 시간 => 키 유효기간 설정을 하기 위해
				.setIssuedAt(new Date())
				// 발급한 키 만료시간 설정 => 60*60*1000
				.setExpiration(new Date(System.currentTimeMillis()+3600000))
				.signWith(Keys.hmacShaKeyFor(SECRET.getBytes()))
				.compact();
	}
	
	// 사용자 아이디 추출
	public String getUsername(String token)
	{
		return Jwts.parserBuilder()
				.setSigningKey(SECRET.getBytes())
				.build()
				.parseClaimsJwt(token)
				.getBody()
				.getSubject();
	}
	// 위조 방지
	public boolean validate(String token)
	{
		try
		{
			Jwts.parserBuilder()
			.setSigningKey(SECRET.getBytes())
			.build()
			.parseClaimsJwt(token);
			
			return true;
		} 
		catch (Exception ex) 
		{
			ex.printStackTrace();
			return false;
		}
	}
	
}
