package com.sist.web.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import com.sist.web.service.CustomUserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JWTAuthenticationFilter extends OncePerRequestFilter{

	private final CustomUserDetailsService uds;
	private final JWTAuthenticationProvider provider;
	
	// 사용자 정보가 저장되는 곳 => UserDetailsService
	// 토큰 생성과 유효성 검사를 해주는 곳 => Provider
	// 통합 => Filter
	// 권한과 URL 접근이 가능한 곳 => Config
	// 실제 사용자 요청을 받는 곳 => Controller
	public JWTAuthenticationFilter( CustomUserDetailsService uds, JWTAuthenticationProvider provider)
	{
		this.uds = uds;
		this.provider = provider;
	}
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// 인증을 거친다
		String token = null;
		// 1. header에 토큰이 저장됨 => vue/react에서 요청하면 헤더를 통해서 값이 들어오기도 함 
		// vue/react는 단독으로 쓰면 따로 세션을 저장? 왜냐면 포트번호가 달라서
		// 2. cookie 혹은 여기 쿠키에 저장돼 있기도 함
		
		String header = request.getHeader("Authorization"); // 헤더 이름은 JSON형식으로 됨
		// "Authorization" => 키 이름
		// {"Authorization":"Bearer 여기가생성된토큰자리"} => subject:id / role:권한
		
		if(header!=null && header.startsWith("Bearer "))
		{
			token = header.substring(7);
		}
		
		// 헤더가 안 넘어올 떄는 쿠키처리
		if(token==null && request.getCookies()!=null)
		{
			for(Cookie cookie:request.getCookies())
			{
				if("accessToken".equals(cookie.getName()))
				{
					token = cookie.getValue();
					break;
				}
			}
		}
		
		// JWT 검증
		if(token!=null && provider.validate(token))
		{
			// 사용자 정보 조회
			String username = provider.getUsername(token);
			// 사용자 정보 추출
			UserDetails user = uds.loadUserByUsername(username);
			// security에서 인증
			UsernamePasswordAuthenticationToken auth =
					                       new UsernamePasswordAuthenticationToken(
					                    		   user, // 사용자 정보
					                    		   null,// 자격 정보
					                    		   user.getAuthorities()// 권한
					                    		   );
			// 저장된 정보를 security에서 관리
			SecurityContextHolder.getContext()
			                      .setAuthentication(auth);
		}
		// Controller(다음 Filter)에서 사용이 가능하게 실행
		filterChain.doFilter(request, response);
		// 초창기엔 요청을 받으면 => DispatcherServlet으로 이동
		// security가 있을 땐 => 요청 - Security - DispatcherServlet 
	}

}
