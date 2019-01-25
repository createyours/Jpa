package org.leadingsoft.golf.api.model;

public class Collect {
  /** 募集ID */
  private String roundSerialNo;
  /** 主催者ID */
  private String memberID;
  /** コースID */
  private String courseID;
  /** コース名 */
  private String courseName;
  /** ゴルフ場名 */
  private String golfCourseName;
  /** スタートコース */
  private String startCourse;
  /** プレイ日 */
  private String playDate;
  /** スタート時間 */
  private String startTime;
  /** プレースタイル */
  private String playStyle;
  /** プレー料金 */
  private String playFee;
  /** ランチ含む */
  private String lunchFlag;
  /** 予約詳細 */
  private String roundDetails;
  /** 募集人数 */
  private String recruitNum;
  /** 募集範囲 */
  private String recruitRange;
  /** 参加者へのコメント */
  private String coments;
  /** 締切日時 */
  private String closeDate;
  /** 通知希望フラグ */
  private String pushFlag;
  /** 募集進行状況 */
  private String status;
  /** 登録日時 */
  private String regDate;

  public String getRoundSerialNo() {
    return roundSerialNo;
  }

  public void setRoundSerialNo(String roundSerialNo) {
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

  public String getCourseName() {
    return courseName;
  }

  public void setCourseName(String courseName) {
    this.courseName = courseName;
  }

  public String getStartCourse() {
    return startCourse;
  }

  public void setStartCourse(String startCourse) {
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

  public String getPlayFee() {
    return playFee;
  }

  public void setPlayFee(String playFee) {
    this.playFee = playFee;
  }

  public String getLunchFlag() {
    return lunchFlag;
  }

  public void setLunchFlag(String lunchFlag) {
    this.lunchFlag = lunchFlag;
  }

  public String getRoundDetails() {
    return roundDetails;
  }

  public void setRoundDetails(String roundDetails) {
    this.roundDetails = roundDetails;
  }

  public String getRecruitNum() {
    return recruitNum;
  }

  public void setRecruitNum(String recruitNum) {
    this.recruitNum = recruitNum;
  }

  public String getRecruitRange() {
    return recruitRange;
  }

  public void setRecruitRange(String recruitRange) {
    this.recruitRange = recruitRange;
  }

  public String getComents() {
    return coments;
  }

  public void setComents(String coments) {
    this.coments = coments;
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

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getRegDate() {
    return regDate;
  }

  public void setRegDate(String regDate) {
    this.regDate = regDate;
  }

  public String getGolfCourseName() {
    return golfCourseName;
  }

  public void setGolfCourseName(String golfCourseName) {
    this.golfCourseName = golfCourseName;
  }
}
