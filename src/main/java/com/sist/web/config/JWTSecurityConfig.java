package com.sist.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
/*
 *   사용자(/member/login)
 *   |
 *   login.html
 *   
 *   => Spring security는 id나 pwd를 username,password로 인식한다
 *   
 *   |
 *   AuthenticationManager
 *   |
 *   UserDetailsService
 *   |
 *   DB(마이바티스)
 *   |
 *   인증완료
 *   |
 *   JWTProvider
 *   |
 *   JWT 토큰 생성
 *   |
 *   JWT 토근 발급
 *   |
 *   메인 페이지로 이동
 * 
 */
import org.springframework.security.web.SecurityFilterChain;

import com.sist.web.security.JWTAuthenticationFilter;
import com.sist.web.security.JWTAuthenticationProvider;
import com.sist.web.service.CustomUserDetailsService;
@Configuration
@EnableWebSecurity
public class JWTSecurityConfig { 
	
    @Bean // 클래스 메모리할당할 때 사용
    public JWTAuthenticationFilter jwtAuthenticationFilter(
    		CustomUserDetailsService uds, JWTAuthenticationProvider provider)
    {
    	return new JWTAuthenticationFilter(uds,provider);
    }
    
	// 시큐리티 라이브러리 추가됐을 때 메인 접속하자마자 나오는 로그인창 없애기 위해
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http)
	throws Exception
	{
		http
		.csrf(csrf-> csrf.disable()) // 위조방지
		.formLogin(form-> form.disable())
		.httpBasic(basic-> basic.disable()) // 나오자마자 로그인창 뜨는 거 없앰
		.authorizeHttpRequests(auth-> auth.requestMatchers("/","/login","/member")
				.permitAll()
				// 스프링 시큐리티의 requestMatchers는 무조건 /로 시작하는 절대 경로 형식만 인정하고 인식함 
				.requestMatchers("/admin").hasRole("ADMIN")
				.anyRequest().permitAll()
				);
		return http.build();
	}
}
