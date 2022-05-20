package com.cogus.cjgourmet.admin.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.cogus.cjgourmet.admin.service.AdminService;
import com.cogus.cjgourmet.admin.vo.AdminVO;
import com.cogus.cjgourmet.admin.vo.EventCntVO;
import com.cogus.cjgourmet.admin.vo.ListCriteria;
import com.cogus.cjgourmet.admin.vo.ListPageVO;
import com.cogus.cjgourmet.event.vo.EventVO;

import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Controller
@RequestMapping("/api/manager")
@RequiredArgsConstructor
public class AdminAPIController {

	@Setter(onMethod_=@Autowired)
	AdminService service;
	
//	@Setter(onMethod_=@Autowired)
//	PasswordEncoder passwordEncoder;
	
	@PostMapping("/add")
	public String add(AdminVO admin, RedirectAttributes rttr) {
		System.out.println("회원가입 페이지 post");
		
		//비밀번호 암호화
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String hashPwd = encoder.encode(admin.getPassword());
		admin.setPassword(hashPwd);
		
		if(service.register(admin)) {
			rttr.addFlashAttribute("result", "회원가입에 성공했습니다");
			return "redirect:/manager";
			
		} else {
			rttr.addFlashAttribute("result", "다시 시도해주세요");
			return "redirect:/manager/join";
		}
	}
	
	@GetMapping("/idCheck")
	@ResponseBody
	public int idCheck(String adminId) {
		return service.idCheck(adminId);
	}
	
	@PostMapping("/login")
	public String login(AdminVO admin, HttpServletRequest request, RedirectAttributes rttr) {
		String adminId = admin.getAdminId();
		
		//입력한 id에 일치하는 adminVO가 있다면
		if(service.login(adminId) != null) {
			AdminVO adminvo = service.login(adminId);
			
			//session 생성
			HttpSession session = request.getSession(true);
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			
			if(
				//입력한 id와 pw가 일치하는지 확인 
				//pw는 encoder import 후 matches 이용해서 확인
				admin.getAdminId().equals(adminvo.getAdminId()) &&
				encoder.matches(admin.getPassword(), adminvo.getPassword())
			) {
				//일치한다면 session에 adminVO 저장
				session.setAttribute("admin", adminvo);
				return "redirect:/manager/eventList";
			} else {
				//일치하지 않다면 다시 로그인 페이지 이동
				rttr.addFlashAttribute("result", "로그인에 실패하였습니다");
				return "redirect:/manager";
			}
		} else {
			//입력한 id에 일치하는 adminVO가 없다면 다시 로그인 페이지 이동
			rttr.addFlashAttribute("result", "로그인에 실패하였습니다");
			return "redirect:/manager";
		}
	}
	
	@GetMapping("/logout")
	public String logout(HttpServletRequest request) {
	    HttpSession session = request.getSession(false);
	    
	    if (session != null) {
	    	// 세션 삭제
	        session.invalidate();   
	    }
	    return "redirect:/manager";
	}

	@PostMapping("/indexList")
	@ResponseBody
	public Map index(ListCriteria cri) {
		System.out.println("이벤트리스트 페이지 post");
		System.out.println(cri);
		
		//참여자 수
		List<EventCntVO> eventCnt2 = service.getCount2();
		
		//참여자 수(중복 제거)
		List<EventCntVO> eventCnt = service.getCount();
		
		//참여자 리스트
		List<EventVO> evnetList = service.getList(cri);
		for (EventVO vo : evnetList) {
			System.out.println(vo);
		}
		
		//map에 list들 넣어서 이동
		Map<String, Object> event = new HashMap<String, Object>();
		event.put("cnt2", eventCnt2);
		event.put("cnt", eventCnt);
		event.put("list", evnetList);
		
		//page정보도 map에 넣어서 이동
		int total = service.getListCnt(cri);
		ListPageVO page = new ListPageVO(cri, total);
		event.put("page", page);
		
		System.out.println(page);
		return event;
	}
	
	@PostMapping("/remove")
	@ResponseBody
	public Map remove(ListCriteria cri, int eno) {
		System.out.println("게시글 삭제 post");
		System.out.println(eno+"번 게시글 삭제");
		
		boolean result = service.remove(eno);
		
		//map에 cri와 result결과 넣어서 이동
		Map<String, Object> event = new HashMap<String, Object>();
		event.put("cri", cri);
		event.put("result", result);
		
		return event;
	}
	
	@GetMapping("/excel")
	public void excelDownload(ListCriteria cri, HttpServletResponse response) throws IOException {
		Workbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet("First");
		
		CellStyle style = wb.createCellStyle();
		Font font = wb.createFont();
		font.setFontName("Calibri"); //폰트이름
		style.setFont(font);
		
		Row row = null;
		Cell cell = null;
		int rowNum = 0;
		int cellNum = 0;
		
		//------------------참여자 수------------------
//		List<EventCntVO> eventCnt = service.getCount();
//		
//		// Header
//        row = sheet.createRow(rowNum++); //0행
//        cell = row.createCell(0); //0열
//        cell.setCellValue("이벤트 참여인원");
//        
//        String[] headers = {"참여날짜", "참여인원"};
//        
//        row = sheet.createRow(rowNum++); //1행
//        for (String header : headers) {
//            cell = row.createCell(cellNum++);
//            cell.setCellValue(header);
//		}
//        
//        // Body
//        for(int i = 0;i<eventCnt.size()-1;i++) {
//        	cellNum = 0;
//        	row = sheet.createRow(rowNum++); //2, 3, 4,, 행
//        	cell = row.createCell(cellNum++);
//            cell.setCellValue(eventCnt.get(i).getRegDate());
//            cell = row.createCell(cellNum++);
//            cell.setCellValue(eventCnt.get(i).getCnt());
//        }
//        
//        //총 합계 footer
//        cellNum = 0;
//        row = sheet.createRow(rowNum++);
//        cell = row.createCell(cellNum++);
//        cell.setCellValue("총 참여인원");
//        cell = row.createCell(cellNum++);
//        cell.setCellValue(eventCnt.get(eventCnt.size()-1).getCnt());
        
        //-----------------참여자 리스트-----------------
      	List<EventVO> eventList = service.getData(cri);
      	
      	// Header
      	cellNum = 0;
        row = sheet.createRow(rowNum++);
        cell = row.createCell(0); //0열
        cell.setCellValue("이벤트 참여자 리스트");
        
        String[] headers2 = {"NO", "이름", "타입", "텍스트", "IP주소", "파일이름", "참여시간"};
        
        row = sheet.createRow(rowNum++);
        for (String header : headers2) {
            cell = row.createCell(cellNum++);
            cell.setCellValue(header);
		}
		
        // Body
        int no = 1;
        for (EventVO vo : eventList) {
        	System.out.println(vo);
        	cellNum = 0;
        	row = sheet.createRow(rowNum++); //2, 3, 4,, 행
        	cell = row.createCell(cellNum++);
            cell.setCellValue(no++);
            cell = row.createCell(cellNum++);
            cell.setCellValue(vo.getId());
            cell = row.createCell(cellNum++);
            cell.setCellValue(vo.getType());
            cell = row.createCell(cellNum++);
            cell.setCellValue(vo.getContent());
            cell = row.createCell(cellNum++);
            cell.setCellValue(vo.getIpAddress());
            cell = row.createCell(cellNum++);
            cell.setCellValue(vo.getFileName());
            
            String regDate = vo.getRegDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            cell = row.createCell(cellNum++);
            cell.setCellValue(regDate);
		}
        
        // 컨텐츠 타입과 파일명 지정
        response.setContentType("ms-vnd/excel");
        response.setHeader("Content-Disposition", "attachment;filename=cj_eventList.xlsx");
        
        // Excel File Output
        wb.write(response.getOutputStream());
        wb.close();
	}
}
