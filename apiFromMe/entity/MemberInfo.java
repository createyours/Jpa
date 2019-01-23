package org.leadingsoft.golf.api.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "Memberinfo")
public class MemberInfo implements Serializable {

  /** serialVersionUID */
  private static final long serialVersionUID = -6770855329143951456L;//实例唯一化

  @Id
  /** 会員ID */
  private String memberID;
  /** メールアドレス */
  private String email;
  /** パスワード */
  private String password;
  /** 表示名 */
  private String nickname;
  /** 氏名 */
  private String name;
  /** カナ名 */
  private String kana;
  /** 性別 */
  private String sex;
  /** 生年月日 */
  private String birthday;
  /** ゴルフ年数 */
  private String playYears;
  /** 平均スコア */
  private String averageScore;
  /** スタイル */
  private String playStyle;
  /** 自己紹介 */
  private String notes;
  /** 携帯番号 */
  private String telNo;
  /** 国 */
  private String country;
  /** 都道府県 */
  private String state;
  /** 住所 */
  private String address;
  /** 使用するドライバー */
  private String gear1;
  /** 使用するアイアン */
  private String gear2;
  /** 使用するパター */
  private String gear3;
  /** よく行くゴルフ場の県１ */
  private String area1;
  /** よく行くゴルフ場の県２ */
  private String area2;
  /** 好みのゴルフ場１ */
  private String club1;
  /** 好みのゴルフ場２ */
  private String club2;
  /** 好みのゴルフ場３ */
  private String club3;
  /** 好みのゴルフ場４ */
  private String club4;
  /** 好みのゴルフ場５ */
  private String club5;
  /** ゴルフできる曜日１ */
  private String day1;
  /** ゴルフできる曜日２ */
  private String day2;
  /** ゴルフできる曜日３ */
  private String day3;
  /** 通知フラグ */
  private String pushFlag;

  public String getMemberID() {
    return memberID;
  }

  public void setMemberID(String memberID) {
    this.memberID = memberID;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
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

  public String getBirthday() {
    return birthday;
  }

  public void setBirthday(String birthday) {
    this.birthday = birthday;
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

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getGear1() {
    return gear1;
  }

  public void setGear1(String gear1) {
    this.gear1 = gear1;
  }

  public String getGear2() {
    return gear2;
  }

  public void setGear2(String gear2) {
    this.gear2 = gear2;
  }

  public String getGear3() {
    return gear3;
  }

  public void setGear3(String gear3) {
    this.gear3 = gear3;
  }

  public String getArea1() {
    return area1;
  }

  public void setArea1(String area1) {
    this.area1 = area1;
  }

  public String getArea2() {
    return area2;
  }

  public void setArea2(String area2) {
    this.area2 = area2;
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

  public String getClub5() {
    return club5;
  }

  public void setClub5(String club5) {
    this.club5 = club5;
  }

  public String getDay1() {
    return day1;
  }

  public void setDay1(String day1) {
    this.day1 = day1;
  }

  public String getDay2() {
    return day2;
  }

  public void setDay2(String day2) {
    this.day2 = day2;
  }

  public String getDay3() {
    return day3;
  }

  public void setDay3(String day3) {
    this.day3 = day3;
  }

  public String getPushFlag() {
    return pushFlag;
  }

  public void setPushFlag(String pushFlag) {
    this.pushFlag = pushFlag;
  }

}
