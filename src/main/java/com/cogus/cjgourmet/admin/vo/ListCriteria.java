package com.cogus.cjgourmet.admin.vo;

import org.springframework.web.util.UriComponentsBuilder;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@AllArgsConstructor
@ToString
public class ListCriteria {
	private int pageNum;
	private int perPage;
	private String keyword;
	private String sType;
	private String eatType;
	
	
	public int getStartCount() {
		return (this.pageNum - 1) * perPage;
	}
	
	public ListCriteria() {
		this(1, 5);
	}

	public ListCriteria(int pageNum, int perPage) {
		this.pageNum = pageNum;
		this.perPage = perPage;
	}
	
	public String getListLink() {
		UriComponentsBuilder builder = UriComponentsBuilder.fromPath("")
				.queryParam("pageNum", this.getPageNum())
				.queryParam("perPage", this.getPerPage())
				.queryParam("keyword", this.getKeyword())
				.queryParam("sType", this.getSType())
				.queryParam("eatType", this.getEatType());
		return builder.toUriString();
	}
}
