package org.leadingsoft.golf.api.model;

public class ApplyInfo {
  /** 募集ID */
  private String roundSerialNo;
  /** 応募者ID */
  private String memberID;
  /** 受付通番 */
  private String regNo;
  /** 主催者へのコメント */
  private String comments;
  /** キャンセルフラグ */
  private String cancelFlag;
  /** キャンセル待ちフラグ */
  private String waitFlag;
  /** チャット内容 */
  private String chatContents;
  /** 承認ステータス */
  private String apovStatus;
  /** 氏名 */
  private String name;
  /** カナ名 */
  private String kana;
  /** 性別 */
  private String sex;
  /** 年齢 */
  private String playYears;
  /** 携帯番号 */
  private String telNo;
  /** 都道府県 */
  private String state;

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

  public String getRegNo() {
    return regNo;
  }

  public void setRegNo(String regNo) {
    this.regNo = regNo;
  }

  public String getComments() {
    return comments;
  }

  public void setComments(String comments) {
    this.comments = comments;
  }

  public String getCancelFlag() {
    return cancelFlag;
  }

  public void setCancelFlag(String cancelFlag) {
    this.cancelFlag = cancelFlag;
  }

  public String getWaitFlag() {
    return waitFlag;
  }

  public void setWaitFlag(String waitFlag) {
    this.waitFlag = waitFlag;
  }

  public String getChatContents() {
    return chatContents;
  }

  public void setChatContents(String chatContents) {
    this.chatContents = chatContents;
  }

  public String getApovStatus() {
    return apovStatus;
  }

  public void setApovStatus(String apovStatus) {
    this.apovStatus = apovStatus;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getKana() {
    return kana;
  }

  public void setKana(String kana) {
    this.kana = kana;
  }

  public String getSex() {
    return sex;
  }

  public void setSex(String sex) {
    this.sex = sex;
  }

  public String getPlayYears() {
    return playYears;
  }

  public void setPlayYears(String playYears) {
    this.playYears = playYears;
  }

  public String getTelNo() {
    return telNo;
  }

  public void setTelNo(String telNo) {
    this.telNo = telNo;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

}
