package com.sist.web.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.sist.web.service.MemberService;
import com.sist.web.vo.MemberVO;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MainController {
	private final MemberService mService; // 전체 정보 갖고 있음
	
	@GetMapping("/")
	public String main_main
	( Authentication auth, Model model)
	{
		// 로그인 트루조건
		boolean isLogin=auth!=null && auth.isAuthenticated() && auth.getPrincipal()
				                          .toString().equals("annoymousUser")==false;
		model.addAttribute("isLogin",isLogin);
		if(isLogin)
		{
			String username = auth.getName();
			MemberVO vo =mService.findByUsername(username);
			String role = auth.getAuthorities().iterator()
					           .next().getAuthority();
			model.addAttribute("username",vo.getName());
			model.addAttribute("role",role);
		}
		return "main/main";
	}
	
	// 로그인 화면만 이동
	@GetMapping("/member/login")
	public String member_login(Model model)
	{
		
		return "member/login";
	}
}
