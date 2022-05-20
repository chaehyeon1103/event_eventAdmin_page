package com.cogus.cjgourmet.event.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.swing.filechooser.FileSystemView;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.cogus.cjgourmet.event.service.EventService;
import com.cogus.cjgourmet.event.vo.EventVO;

import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Controller
@RequestMapping("/api/event")
@RequiredArgsConstructor
public class EventAPIController {

	@Setter(onMethod_=@Autowired)
	EventService service;
	
	@PostMapping("/add")
	@ResponseBody
	public boolean add(EventVO event, MultipartFile uploadFile, HttpServletRequest request) throws IllegalStateException, IOException {
		System.out.println("이벤트 페이지 등록 post");
		
		//ip 가져오기
		String ip = request.getHeader("X-FORWARDED-FOR"); 
	    //proxy 환경일 경우
	    if (ip == null || ip.length() == 0) {
	        ip = request.getHeader("Proxy-Client-IP");
	    }
	    //웹로직 서버일 경우
	    if (ip == null || ip.length() == 0) {
	        ip = request.getHeader("WL-Proxy-Client-IP");
	    }
	    if (ip == null || ip.length() == 0) {
	        ip = request.getRemoteAddr();
	    }
		event.setIpAddress(ip);
		
		//기존 파일명
		String fileName = uploadFile.getOriginalFilename();
		//확장자
		String fileExt = fileName.substring(fileName.lastIndexOf(".")+1);
		//uuid 생성
		String uuid = UUID.randomUUID().toString();
		//파일명 치환
		fileName = uuid+"."+fileExt;
		event.setFileName(fileName);
		
		String uploadFolder = "D:\\uploadImg";
		
		File saveFile = new File(uploadFolder, fileName);
		uploadFile.transferTo(saveFile);
		
		if(service.register(event)) {
			return true;
		} else {
			return false;
		}
	}

	@PostMapping("/indexList")
	@ResponseBody
	public List<EventVO> index(int page) {
		int perPage = 8;
		int startCount = (page - 1) * perPage;
		return service.getList(perPage, startCount);
	}
	
	@GetMapping("/display")
	@ResponseBody
	public ResponseEntity<byte[]> getFile(String fileName){
		File file = new File("D:\\uploadImg\\" + fileName);
        ResponseEntity<byte[]> result = null;
        try {
            HttpHeaders header = new HttpHeaders();
            header.add("Content-type", Files.probeContentType(file.toPath()));
            result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file),
            		header, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
	
	@GetMapping("/getCount")
	@ResponseBody
	public int getCounct() {
		return service.getCount();
	}
	
}
