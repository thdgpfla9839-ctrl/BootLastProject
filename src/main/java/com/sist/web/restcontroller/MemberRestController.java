package com.sist.web.restcontroller;


import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sist.web.security.JWTAuthenticationProvider;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MemberRestController {

	private final AuthenticationManager manager;
	private final JWTAuthenticationProvider provider;
	
	// 로그인 성공여부 확인
	@RequestMapping("/member/login_ok")
	public ResponseEntity<?> login(@RequestParam(value = "username",required = false) String username,
			                       @RequestParam(value = "password",required = false) String password)
	{
		try 
		{
			// 아이디랑 비밀번호 인증
			Authentication auth = manager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
			System.out.println("아이디와 비밀번호 인증이 시작됨");
			
			// 인증된 사용자 정보 가져오기
			// 시큐리티context에 사용자 정보가 저장돼 있음
			UserDetails user = (UserDetails)auth.getPrincipal();
			System.out.println("인증된 사용자 정보");
			// 사용자 권한 가져오기
			String role = user.getAuthorities()
					.iterator()
					.next()
					.getAuthority();
			 System.out.println("사용자 권한:"+role);
			// JWT 생성 => 토큰 발급
			String token = provider.createToken(user.getUsername(), role);
			// JWT 쿠키 생성
			ResponseCookie cookie = ResponseCookie.from("accessToken",token).httpOnly(true)
					                    .secure(false)
					                    .path("/")
					                    .maxAge(3600)
					                    .build();
			System.out.println("JWT Cookie:"+cookie);
			// 로그인 성공 여부 확인
			return ResponseEntity.status(HttpStatus.FOUND)
					             .header(HttpHeaders.SET_COOKIE, cookie.toString())
					             .header(HttpHeaders.LOCATION, "/").build();
			
		} 
		// 로그인 실패처리
		// 로그인 실패 => 아이디 / 비밀번호	
		catch (BadCredentialsException ex) 
		{
			
			return ResponseEntity.status(HttpStatus.FOUND)
		             .header(HttpHeaders.LOCATION, "/member/login?error=true").build();
		}
		 // 인증 실패
		catch(AuthenticationException ex)
		{
			return ResponseEntity.status(HttpStatus.FOUND)
		             .header(HttpHeaders.LOCATION, "/member/login?error=true").build();
		}
		// 서버 오류
		catch(Exception ex)
		{
			return ResponseEntity.status(HttpStatus.FOUND)
		             .header(HttpHeaders.LOCATION, "/member/login?error=true").build();
	}
}
	@GetMapping("/member/logout")
	public ResponseEntity<Void> logout(){
		// 쿠키 삭제
		ResponseCookie cookie = ResponseCookie.from("accessToken","")
				                  .httpOnly(true)
				                  .secure(false)
				                  .path("/")
				                  .maxAge(0)
				                  .build();
		
		return ResponseEntity.status(HttpStatus.FOUND)
				              .header(HttpHeaders.SET_COOKIE, cookie.toString())
				              .header(HttpHeaders.LOCATION, "/").build();
	}

}