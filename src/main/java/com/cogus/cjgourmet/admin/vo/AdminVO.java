package com.cogus.cjgourmet.admin.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AdminVO {
	private int ano;
	private String adminId;
	private String password;
	private String role;
}
