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

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * <pre>
 * 楽天ゴルフ場基本情報エンティティ
 * </pre>
 */
@Data
@Entity
@Table(name="RakutenGolfCourseInfo")
public class RakutenGolfCourseInfo implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 8752549003811290133L;

	/** ゴルフ場ID */
	@Id
	@JsonProperty(value = "GolfCourseId")
	private String golfCourseId;

	/** ゴルフ場名 */
	@JsonProperty(value = "GolfCourseName")
	private String golfCourseName;

	/** ゴルフ場名(略称) */
	@JsonProperty(value = "GolfCourseAbbr")
	private String golfCourseAbbr;

	/** かな名 */
	@Column(name = "Name_k")
	@JsonProperty(value = "Name_k")
	private String namek;

	/** 都道府県 */
	@JsonProperty(value = "State")
	private String state;

	/** 住所 */
	@JsonProperty(value = "Address")
	private String address;

	/** 電話番号 */
	@JsonProperty(value = "Tel")
	private String tel;

	/** 経度 */
	@JsonProperty(value = "Longitude")
	private Float longitude;

	/** 緯度 */
	@JsonProperty(value = "Latitude")
	private Float latitude;

	/** 説明 */
	@JsonProperty(value = "Caption")
	private String caption;

	/** コースタイプ:山岳、丘陵、林間など */
	@JsonProperty(value = "Type")
	private Integer type;

	/** 評価１:楽天評価 */
	@JsonProperty(value = "Point1")
	private Float point1;

	/** 評価２:GDO評価 */
	@JsonProperty(value = "Point2")
	private Float point2;

	/** 評価３:その他評価 */
	@JsonProperty(value = "Point3")
	private Float point3;

	/** その他 */
	@JsonProperty(value = "Other")
	private String other;

}
