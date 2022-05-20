package com.cogus.cjgourmet.event.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/event")
@RequiredArgsConstructor
public class EventController {
	
	@GetMapping
	public String eventGet() {
		System.out.println("이벤트 페이지 get");
		return "event/cjgourmet";
	}
}
