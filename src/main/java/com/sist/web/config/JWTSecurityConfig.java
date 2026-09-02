package com.sist.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password4j.BcryptPassword4jPasswordEncoder;
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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
	public SecurityFilterChain securityFilterChain(HttpSecurity http, JWTAuthenticationFilter filter)
	throws Exception
	{
		http
		.csrf(csrf-> csrf.disable()) // 위조방지
		.sessionManagement(session-> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		.formLogin(form-> form.disable())
		//.httpBasic(basic-> basic.disable()) // 나오자마자 로그인창 뜨는 거 없앰
		.authorizeHttpRequests(auth-> auth.requestMatchers("/","/login","/member")
				.permitAll()
				// 스프링 시큐리티의 requestMatchers는 무조건 /로 시작하는 절대 경로 형식만 인정하고 인식함 
				.requestMatchers("/admin").hasRole("ADMIN")
				.anyRequest().permitAll()
				)
		.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}
	
	// 비밀번호 암호화
	@Bean
	public PasswordEncoder passwordEncoder()
	{
		// 암호화를 하고싶을 때는 => encode()
		// 검색을 하고 싶을 때 => matcher()
		// 같은 비밀번호가 들어갔을 때는 여러개의 패턴을 갖고 만들어서 다르다는 걸 기억하기 => 암호화된 비밀번호 생김새가 다르다는 의미
		return new BCryptPasswordEncoder();
	}
	// 인가 관리자 등록
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
	throws Exception
	{
		return config.getAuthenticationManager();
	}
}
