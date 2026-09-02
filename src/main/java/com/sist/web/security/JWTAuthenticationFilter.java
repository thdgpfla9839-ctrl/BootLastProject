package com.sist.web.security;

import java.io.IOException;

import org.springframework.web.filter.OncePerRequestFilter;

import com.sist.web.service.CustomUserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JWTAuthenticationFilter extends OncePerRequestFilter{

	private final CustomUserDetailsService uds;
	private final JWTAuthenticationProvider provider;
	
	public JWTAuthenticationFilter( CustomUserDetailsService uds, JWTAuthenticationProvider provider)
	{
		this.uds = uds;
		this.provider = provider;
	}
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
	}

}
