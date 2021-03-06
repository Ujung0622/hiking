package project.admin.e_p006.vo;

import org.springframework.stereotype.Component;

@Component
public class E_p006VO {
	//cs글 통계
	private String csposttypeString; // 글구분 스트링
	private int csboardcnt; // 글 구분 카운트
	
	//결제 통계
	private String createdatString; // 매출일자
	private int sumPrice; // 매출 금액
	
	//회원 통계
	private String userCreatedat; // 회원 가입일자
	private int man; // 남자
	private int woman; // 여자
	private int allUser; //전체 회원수
	
	//카테고리 통계
	private String ordereDatString; //판매일자
	private int type1; //의류합계
	private int type2; //잡화합계
	private int type3; //등산용품합계
	
	//메인페이지 1달기준 총 매출 및 취소매출 통계
	private String datString; //날짜 스트링변환
	private String totalPrice; //승인 매출
	private String totalCancel; //취소 매출
	
	//메인페이지 카테고리별 조회수 파이 차트
	private String categorType; //카테고리 타입
	private int sumCnt; //조회수 합

	
	
	public String getCsposttypeString() {
		return csposttypeString;
	}
	public void setCsposttypeString(String csposttypeString) {
		this.csposttypeString = csposttypeString;
	}
	public int getCsboardcnt() {
		return csboardcnt;
	}
	public void setCsboardcnt(int csboardcnt) {
		this.csboardcnt = csboardcnt;
	}
	public String getCreatedatString() {
		return createdatString;
	}
	public void setCreatedatString(String createdatString) {
		this.createdatString = createdatString;
	}
	public int getSumPrice() {
		return sumPrice;
	}
	public void setSumPrice(int sumPrice) {
		this.sumPrice = sumPrice;
	}
	public int getMan() {
		return man;
	}
	public void setMan(int man) {
		this.man = man;
	}
	public int getWoman() {
		return woman;
	}
	public void setWoman(int woman) {
		this.woman = woman;
	}
	public int getAllUser() {
		return allUser;
	}
	public void setAllUser(int allUser) {
		this.allUser = allUser;
	}
	public String getUserCreatedat() {
		return userCreatedat;
	}
	public void setUserCreatedat(String userCreatedat) {
		this.userCreatedat = userCreatedat;
	}
	public String getOrdereDatString() {
		return ordereDatString;
	}
	public void setOrdereDatString(String ordereDatString) {
		this.ordereDatString = ordereDatString;
	}
	public int getType1() {
		return type1;
	}
	public void setType1(int type1) {
		this.type1 = type1;
	}
	public int getType2() {
		return type2;
	}
	public void setType2(int type2) {
		this.type2 = type2;
	}
	public int getType3() {
		return type3;
	}
	public void setType3(int type3) {
		this.type3 = type3;
	}
	public String getDatString() {
		return datString;
	}
	public void setDatString(String datString) {
		this.datString = datString;
	}
	public String getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(String totalPrice) {
		this.totalPrice = totalPrice;
	}
	public String getTotalCancel() {
		return totalCancel;
	}
	public void setTotalCancel(String totalCancel) {
		this.totalCancel = totalCancel;
	}
	public String getCategorType() {
		return categorType;
	}
	public void setCategorType(String categorType) {
		this.categorType = categorType;
	}
	public int getSumCnt() {
		return sumCnt;
	}
	public void setSumCnt(int sumCnt) {
		this.sumCnt = sumCnt;
	}	
	
}
