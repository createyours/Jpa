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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
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
  @OneToMany
  @JoinColumn(name = "courseID")
  private List<CourseInfo> courseInfos;
  @OneToOne
  @JoinColumn(name = "roundSerialNo",referencedColumnName = "roundserialno")
  private Applyinfo applyinfo;
}