package org.leadingsoft.golf.api.model;

public class Profile {

  /** 会員ID */
  private String memberid;
  /** 表示名 */
  private String nickname;
  /** 氏名 */
  private String name;
  /** カナ名 */
  private String kana;
  /** 性別 */
  private String sex;
  /** 年齢 */
  private String playYears;
  /** 平均スコア */
  private String averageScore;
  /** スタイル */
  private String playStyle;
  /** 自己紹介 */
  private String notes;
  /** 携帯番号 */
  private String telNo;
  /** 都道府県 */
  private String state;
  /** 募集情報通知フラグ */
  private String pushFlag;
  /** 募集情報通知希望ゴルフ場：ゴルフ場１ */
  private String club1;
  /** 募集情報通知希望ゴルフ場：ゴルフ場2 */
  private String club2;
  /** 募集情報通知希望ゴルフ場：ゴルフ場3 */
  private String club3;
  /** 募集情報通知希望ゴルフ場：ゴルフ場4 */
  private String club4;

  public String getMemberid() {
    return memberid;
  }

  public void setMemberid(String memberid) {
    this.memberid = memberid;
  }

  public String getNickname() {
    return nickname;
  }

  public void setNickname(String nickname) {
    this.nickname = nickname;
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

  public String getAverageScore() {
    return averageScore;
  }

  public void setAverageScore(String averageScore) {
    this.averageScore = averageScore;
  }

  public String getPlayStyle() {
    return playStyle;
  }

  public void setPlayStyle(String playStyle) {
    this.playStyle = playStyle;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
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

  public String getPushFlag() {
    return pushFlag;
  }

  public void setPushFlag(String pushFlag) {
    this.pushFlag = pushFlag;
  }

  public String getClub1() {
    return club1;
  }

  public void setClub1(String club1) {
    this.club1 = club1;
  }

  public String getClub2() {
    return club2;
  }

  public void setClub2(String club2) {
    this.club2 = club2;
  }

  public String getClub3() {
    return club3;
  }

  public void setClub3(String club3) {
    this.club3 = club3;
  }

  public String getClub4() {
    return club4;
  }

  public void setClub4(String club4) {
    this.club4 = club4;
  }
}
