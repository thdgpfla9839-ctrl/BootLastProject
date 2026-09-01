package com.sist.web.vo;

import lombok.Data;

/*
 *  NO            NOT NULL NUMBER         
	TITLE         NOT NULL VARCHAR2(1000) 
	INSTRUCTOR_NO NOT NULL NUMBER         
	STAR                   NUMBER(3,1)    
	STUDENT_COUNT          NUMBER         
	PAY_PRICE              NUMBER         
	REGULAR_PRICE          NUMBER         
	CONTENT                CLOB           
	IMAGES                 CLOB           

 */
@Data
public class CourseVO {

	// 마이바티스는 컬럼명이 다르면 as를 해줘야 하므로 잘 맞춰써야한다
	private int no,student_count,pay_price,regular_price;
	private String title,content,images;
	private double star;
}
