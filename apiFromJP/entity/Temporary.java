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
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * <pre>
 * 一時テーブルエンティティ
 * </pre>
 */
@Data
@Entity
@Table(name = "Temporary")
@IdClass(TemporaryId.class)
public class Temporary implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = -5963759943001690211L;

	/** ID */
	@Id
	@JsonProperty(value = "ID")
	@Column(name = "ID")
	private String iD;

	/** タイプ */
	@Id
	@JsonProperty(value = "type")
	private String type;

	/** 一時名 */
	@JsonProperty(value = "TemporaryName")
	private String temporaryName;

	/** 一時値 */
	@JsonProperty(value = "TemporaryValue")
	private String temporaryValue;

	/** 作成日時 */
	@JsonProperty(value = "InsTstmp")
	private String insTstmp;

	/** 有効期限 */
	@JsonProperty(value = "expire")
	private String expire;

}
