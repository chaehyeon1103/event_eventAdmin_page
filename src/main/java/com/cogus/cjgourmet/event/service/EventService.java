package com.cogus.cjgourmet.event.service;

import java.util.List;

import com.cogus.cjgourmet.event.vo.EventVO;

public interface EventService {
	public boolean register(EventVO event);
	public List<EventVO> getList(int perPage, int startCount);
	public int getCount();
}
