//****************************************************************************//
// システム         : Golf
//----------------------------------------------------------------------------//
//                (c)Copyright 2018 LeadingSoft All rights reserved.
//============================================================================//
package org.leadingsoft.golf.api.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * <pre>
 * 募集情報エンティティ
 * </pre>
 */
@Data
@Entity
@Table(name = "RecruitInfo")
public class RecruitInfo implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = -4346659559755870611L;

	/** 募集ID */
	@Id
	private Integer roundSerialNo;

	/** 主催者ID */
	private String memberID;

	/** コースID */
	private String courseID;

	/** スタートコース */
	private Integer startCourse;

	/** プレイ日 */
	private String playDate;

	/** スタート時間 */
	private String startTime;

	/** プレースタイル */
	private String playStyle;

	/** プレー料金 */
	private Integer playFee;

	/** ランチ含む */
	private Integer lunchFlag;

	/** 予約詳細 */
	private String roundDetails;

	/** 募集人数 */
	private Integer recruitNum;

	/** 募集範囲 */
	private Integer recruitRange;

	/** 参加者へのコメント */
	private String coments;

	/** 締切日時 */
	private String closeDate;

	/** 通知希望フラグ */
	private String pushFlag;

	/** 募集進行状況 */
	private Integer status;

	/** 削除フラグ */
	private Integer delFlg;

	/** 登録日時 */
	private String regDate;

}
