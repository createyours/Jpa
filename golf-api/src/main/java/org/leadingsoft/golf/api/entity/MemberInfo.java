package org.leadingsoft.golf.api.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "memberinfo")
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
  
	@OneToMany
	@JoinColumn(name = "memberID")
	private List<Applyinfo> applyInfoList;
  
  @OneToMany
  @JoinColumn(name = "memberID")
  private List<ApplyChat> applyChatList;
  
  
  
}
