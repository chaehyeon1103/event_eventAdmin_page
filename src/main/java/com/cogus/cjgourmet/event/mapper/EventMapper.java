package com.cogus.cjgourmet.event.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.cogus.cjgourmet.event.vo.EventVO;

@Mapper
public interface EventMapper {
	
	public boolean insert(EventVO event);
	public List<EventVO> selectList(int perPage, int startCount);
	public int selectCount();
}
