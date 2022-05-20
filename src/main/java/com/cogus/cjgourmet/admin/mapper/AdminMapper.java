package com.cogus.cjgourmet.admin.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.cogus.cjgourmet.admin.vo.AdminVO;
import com.cogus.cjgourmet.admin.vo.EventCntVO;
import com.cogus.cjgourmet.admin.vo.ListCriteria;
import com.cogus.cjgourmet.event.vo.EventVO;

@Mapper
public interface AdminMapper {
	
	public boolean insert(AdminVO admin);
	public int idCheck(String adminId);
	public AdminVO login(String adminId);
	
	public List<EventCntVO> getCount();
	public List<EventCntVO> getCount2();
	public List<EventVO> getList(ListCriteria cri);
	public int getListCnt(ListCriteria cri);
	public boolean remove(int eno);
	public List<EventVO> getData(ListCriteria cri);
}
