//****************************************************************************//
// システム         : Golf
//----------------------------------------------------------------------------//
//                (c)Copyright 2018 LeadingSoft All rights reserved.
//============================================================================//
package org.leadingsoft.golf.api.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * <pre>
 * コース情報エンティティ
 * </pre>
 */
@Data
@Entity
@Table(name = "CourseInfo")
public class CourseInfo implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 1189961311319701025L;

	/** コースID */
	@Id
	private String id;

	/** コース名 */
	private String name;

	/** かな名 */
	@Column(name = "Name-k")
	private String name_k;

	/** 英語名 */
	@Column(name = "Name-e")
	private String name_e;

	/** ゴルフコース名 */
	private String golfCourseName;

	/** 都道府県 */
	private String state;

	/** 住所 */
	private String address;

	/** 電話番号 */
	private String tel;

	/** 経度 */
	private Float longitude;

	/** 緯度 */
	private Float latitude;

	/** 説明 */
	private String caption;

	/** コースタイプ */
	private Integer type;

	/** 評価１ */
	private Float point1;

	/** 評価２ */
	private Float point2;

	/** 評価３ */
	private Float point3;

	/** その他 */
	private String others;
}
