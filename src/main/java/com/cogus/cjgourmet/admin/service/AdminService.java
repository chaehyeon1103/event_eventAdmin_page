package com.cogus.cjgourmet.admin.service;


import java.util.List;

import com.cogus.cjgourmet.admin.vo.AdminVO;
import com.cogus.cjgourmet.admin.vo.EventCntVO;
import com.cogus.cjgourmet.admin.vo.ListCriteria;
import com.cogus.cjgourmet.event.vo.EventVO;

public interface AdminService {
	public boolean register(AdminVO admin);
	public int idCheck(String adminId);
	public AdminVO login(String adminId);
	
	public List<EventCntVO> getCount();
	public List<EventCntVO> getCount2();
	public List<EventVO> getList(ListCriteria cri);
	public int getListCnt(ListCriteria cri);
	public boolean remove(int eno);
	public List<EventVO> getData(ListCriteria cri);
}
