package com.cogus.cjgourmet.admin.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cogus.cjgourmet.admin.vo.AdminVO;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/manager")
@RequiredArgsConstructor
public class AdminController {
	
	@GetMapping
	public String eventGet() {
		System.out.println("로그인 페이지 get");
		return "admin/login";
	}
	
	@GetMapping("/join")
	public String joinGet() {
		System.out.println("회원가입 페이지 get");
		return "admin/register";
	}
	
	//session이 있다면 eventList 페이지로 이동
	//없다면 다시 login 페이지로 이동
	@GetMapping("/eventList")
	public String eventListGet(
			@SessionAttribute(name = "admin", required = false) AdminVO admin,
			HttpServletRequest request, RedirectAttributes rttr) {
		System.out.println("이벤트리스트 페이지 get");
		
		if(admin == null) {
			rttr.addFlashAttribute("result", "로그인 후 이용 가능합니다");
			return "redirect:/manager";
		}
		return "admin/eventList";
	}
}
