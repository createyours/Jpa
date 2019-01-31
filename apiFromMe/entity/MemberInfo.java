//****************************************************************************//
// システム         : Golf
//----------------------------------------------------------------------------//
//                (c)Copyright 2018 LeadingSoft All rights reserved.
//============================================================================//
package org.leadingsoft.golf.api.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * <pre>
 * 会員情報エンティティ
 * </pre>
 */
@Data
@Entity
@Table(name = "MemberInfo")
public class MemberInfo implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = -6770855329143951456L;

	@Id
	@Column(name = "MemberID")
	@JsonProperty(value = "MemberID")
	/** 会員ID */
	private String memberID;

	/** メールアドレス */
	@JsonProperty(value = "Email")
	private String email;

	/** パスワード */
	@JsonProperty(value = "Password")
	private String passWord;

	/** 表示名 */
	@JsonProperty(value = "Nickname")
	private String nickName;

	/** 氏名 */
	@JsonProperty(value = "Name")
	private String name;

	/** カナ名 */
	@JsonProperty(value = "Kana")
	private String kana;

	/** 性別 */
	@JsonProperty(value = "Sex")
	private String sex;

	/** 生年月日 */
	@JsonProperty(value = "Birthday")
	private String birthday;

	/** ゴルフ年数 */
	@JsonProperty(value = "PlayYears")
	private Integer playYears;

	/** 平均スコア */
	@JsonProperty(value = "AverageScore")
	private Integer averageScore;

	/** スタイル */
	@JsonProperty(value = "PlayStyle")
	private Integer playStyle;

	/** 自己紹介 */
	@JsonProperty(value = "Notes")
	private String notes;

	/** 携帯番号 */
	@JsonProperty(value = "TelNo")
	private String telNo;

	/** 国 */
	@JsonProperty(value = "Country")
	private String country;

	/** 都道府県 */
	@JsonProperty(value = "State")
	private String state;

	/** 住所 */
	@JsonProperty(value = "Address")
	private String address;

	/** 使用するドライバー */
	@JsonProperty(value = "Gear1")
	private String gear1;

	/** 使用するアイアン */
	@JsonProperty(value = "Gear2")
	private String gear2;

	/** 使用するパター */
	@JsonProperty(value = "Gear3")
	private String gear3;

	/** よく行くゴルフ場の県１ */
	@JsonProperty(value = "Area1")
	private String area1;

	/** よく行くゴルフ場の県２ */
	@JsonProperty(value = "Area2")
	private String area2;

	/** 好みのゴルフ場１ */
	@JsonProperty(value = "Club1")
	private String club1;

	/** 好みのゴルフ場２ */
	@JsonProperty(value = "Club2")
	private String club2;

	/** 好みのゴルフ場３ */
	@JsonProperty(value = "Club3")
	private String club3;

	/** 好みのゴルフ場４ */
	@JsonProperty(value = "Club4")
	private String club4;

	/** 好みのゴルフ場５ */
	@JsonProperty(value = "Club5")
	private String club5;

	/** ゴルフできる曜日１ */
	@JsonProperty(value = "Day1")
	private String day1;

	/** ゴルフできる曜日２ */
	@JsonProperty(value = "Day2")
	private String day2;

	/** ゴルフできる曜日３ */
	@JsonProperty(value = "Day3")
	private String day3;

	/** 通知フラグ */
	@JsonProperty(value = "PushFlag")
	private String pushFlag;

	/** 応募情報リスト */
	@JsonIgnore
	@OneToMany(mappedBy="memberInfo",cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	private List<Applyinfo> applyInfoList;

	/** チャットリスト */
	@JsonIgnore
	@OneToMany(mappedBy = "memberInfo")
	private List<ApplyChat> applyChatList;
}
