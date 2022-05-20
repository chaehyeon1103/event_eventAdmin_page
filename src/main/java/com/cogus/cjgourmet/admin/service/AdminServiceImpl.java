package com.cogus.cjgourmet.admin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cogus.cjgourmet.admin.mapper.AdminMapper;
import com.cogus.cjgourmet.admin.vo.AdminVO;
import com.cogus.cjgourmet.admin.vo.EventCntVO;
import com.cogus.cjgourmet.admin.vo.ListCriteria;
import com.cogus.cjgourmet.event.mapper.EventMapper;
import com.cogus.cjgourmet.event.vo.EventVO;

import lombok.Setter;

@Service
public class AdminServiceImpl implements AdminService{

	@Setter(onMethod_=@Autowired)
	private AdminMapper mapper;

	@Override
	public boolean register(AdminVO admin) {
		return mapper.insert(admin);
	}

	@Override
	public int idCheck(String adminId) {
		return mapper.idCheck(adminId);
	}

	@Override
	public AdminVO login(String adminId) {
		return mapper.login(adminId);
	}

	@Override
	public List<EventCntVO> getCount() {
		return mapper.getCount();
	}

	@Override
	public List<EventVO> getList(ListCriteria cri) {
		return mapper.getList(cri);
	}

	@Override
	public int getListCnt(ListCriteria cri) {
		return mapper.getListCnt(cri);
	}

	@Override
	public boolean remove(int eno) {
		return mapper.remove(eno);
	}

	@Override
	public List<EventVO> getData(ListCriteria cri) {
		return mapper.getData(cri);
	}

	@Override
	public List<EventCntVO> getCount2() {
		return mapper.getCount2();
	}
}
