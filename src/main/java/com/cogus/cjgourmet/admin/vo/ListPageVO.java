package com.cogus.cjgourmet.admin.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@AllArgsConstructor
@ToString
public class ListPageVO {
	private int startPage; //시작 페이지
	private int endPage; //끝 페이지
	private boolean prev; //이전, 다음 페이지가 있는지
	private boolean next;
	private int startCount; //시작 게시물 넘버
	private int endCount;
	
	private int total; //총 데이터 갯수
//	private int realEnd; //제일 마지막 페이지
	
	private ListCriteria cri; //페이지 넘버와 페이지당 게시물 수
	
	public ListPageVO(ListCriteria cri, int total) {
		this.cri = cri;
		this.total = total;
		this.startPage = (int)(Math.floor((cri.getPageNum() - 1) / 10) * 10 + 1);
		this.endPage = this.startPage + 9;
		
		//제일 마지막 페이지
		int realEnd = (int)(Math.ceil((total * 1.0) / cri.getPerPage())); 
		if(realEnd < this.endPage) {
			this.endPage = realEnd;
		}
		
		this.startCount = (this.cri.getPageNum() - 1) * this.cri.getPerPage();
		this.endCount = this.total - this.startCount;
		
		
		this.prev = this.startPage > 1;
		this.next = this.endPage < realEnd;
	}
}
