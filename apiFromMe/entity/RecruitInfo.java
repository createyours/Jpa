package org.leadingsoft.golf.api.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;


@Entity
@Table(name = "RecruitInfo")
public class RecruitInfo implements Serializable{
	   

	private static final long serialVersionUID = 1L;
	
  @Id
  //募集ID:通番
  private  int roundSerialNo;
 //主催者ID
  private  String memberID;
 //コースID
  private  String courseID;
 //スタートコース:Out/In
  private  int startCourse;
 //プレイ日
  private  String playDate;
 //スタート時間:セルフ／キャディ付き
  private  String startTime;
 //プレースタイル
  private  String playStyle;
//プレー料金:総額
  private  int playFee;
 //ランチ含む
  private  int lunchFlag;
 //予約詳細
  private  String roundDetails;
 //募集人数:1-3
  private  int recruitNum;
 //募集範囲:1：一般、2：友人、3：親友
  private  int recruitRange = 1;
 //参加者へのコメント
  private  String coments;
 //締切日時
  private  String closeDate;
 //通知希望フラグ:募集範囲が2、3の場合のみ適用
  private  String pushFlag;
 //募集進行状況:0-3：残り人数\n-1：主催者がキャンセルした\n-2：募集締切（時間になったか、主催者が手動で）
  private  int status;
 //0
  private  int delFlg;
 //登録日時
  private  String regDate;
  
  public String getComents() {
		return coments;
	}
	public void setComents(String coments) {
		this.coments = coments;
	}
public int getRoundSerialNo() {
	return roundSerialNo;
}
public void setRoundSerialNo(int roundSerialNo) {
	this.roundSerialNo = roundSerialNo;
}
public String getMemberID() {
	return memberID;
}
public void setMemberID(String memberID) {
	this.memberID = memberID;
}
public String getCourseID() {
	return courseID;
}
public void setCourseID(String courseID) {
	this.courseID = courseID;
}
public int getStartCourse() {
	return startCourse;
}
public void setStartCourse(int startCourse) {
	this.startCourse = startCourse;
}
public String getPlayDate() {
	return playDate;
}
public void setPlayDate(String playDate) {
	this.playDate = playDate;
}
public String getStartTime() {
	return startTime;
}
public void setStartTime(String startTime) {
	this.startTime = startTime;
}
public String getPlayStyle() {
	return playStyle;
}
public void setPlayStyle(String playStyle) {
	this.playStyle = playStyle;
}
public int getPlayFee() {
	return playFee;
}
public void setPlayFee(int playFee) {
	this.playFee = playFee;
}
public int getLunchFlag() {
	return lunchFlag;
}
public void setLunchFlag(int lunchFlag) {
	this.lunchFlag = lunchFlag;
}
public String getRoundDetails() {
	return roundDetails;
}
public void setRoundDetails(String roundDetails) {
	this.roundDetails = roundDetails;
}
public int getRecruitNum() {
	return recruitNum;
}
public void setRecruitNum(int recruitNum) {
	this.recruitNum = recruitNum;
}
public int getRecruitRange() {
	return recruitRange;
}
public void setRecruitRange(int recruitRange) {
	this.recruitRange = recruitRange;
}
public String getCloseDate() {
	return closeDate;
}
public void setCloseDate(String closeDate) {
	this.closeDate = closeDate;
}
public String getPushFlag() {
	return pushFlag;
}
public void setPushFlag(String pushFlag) {
	this.pushFlag = pushFlag;
}
public int getStatus() {
	return status;
}
public void setStatus(int status) {
	this.status = status;
}
public int getDelFlg() {
	return delFlg;
}
public void setDelFlg(int delFlg) {
	this.delFlg = delFlg;
}
public String getRegDate() {
	return regDate;
}
public void setRegDate(String regDate) {
	this.regDate = regDate;
}

	
}