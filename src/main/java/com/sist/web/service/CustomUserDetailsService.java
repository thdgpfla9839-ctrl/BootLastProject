package com.sist.web.service;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sist.web.vo.AuthorityVO;
import com.sist.web.vo.MemberVO;

import groovyjarjarantlr4.v4.parse.ANTLRParser.throwsSpec_return;
import lombok.RequiredArgsConstructor;

// 데이터를 저장하는 곳
@Service // 디비 연동
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService{
	
	private final MemberService mService;
	
	// 데이터를 저장해주는 메소드(권한까지 포함된 2개의 메소드가 있음)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		MemberVO member = mService.findByUsername(username);
		
		// 1. 아이디가 오라클에 존재하지 않는 경우
		if(member==null)
		{
			// 임의로 발생 => 예외처리
			throw new UsernameNotFoundException("사용자를 찾을 수 없습니다:"+username); 
		}
		
		// 2. 휴먼계정인 경우
		if(member.getEnabled()!=1)
		{
			throw new UsernameNotFoundException("휴먼 계정입니다"); 
		}
		List<AuthorityVO> authList = mService.getAuthorityData(member.getMember_id());
		
		// 권한을 저장하기 => springSecurity로 변환
		List<SimpleGrantedAuthority> authorities = authList.stream()
				                                   .map(a-> new SimpleGrantedAuthority(a.getAuthority()))
				                                   .toList();
		// 갖고 온 값을 UserDetails에 저장
		return User.builder()
				.username(member.getUsername())
				.password(member.getPassword())
				.authorities(authorities)
				.build();
	}

	
}
