package com.cogus.cjgourmet.event.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cogus.cjgourmet.event.mapper.EventMapper;
import com.cogus.cjgourmet.event.vo.EventVO;

import lombok.Setter;

@Service
public class EventServiceImpl implements EventService{

	@Setter(onMethod_=@Autowired)
	private EventMapper mapper;

	@Override
	public boolean register(EventVO event) {
		return mapper.insert(event);
	}

	@Override
	public List<EventVO> getList(int perPage, int startCount) {
		return mapper.selectList(perPage, startCount);
	}

	@Override
	public int getCount() {
		return mapper.selectCount();
	}
}
