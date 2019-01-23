//****************************************************************************//
// システム         : Golf
//----------------------------------------------------------------------------//
//                (c)Copyright 2018 LeadingSoft All rights reserved.
//============================================================================//
package org.leadingsoft.golf.api.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * <pre>
 * チャットエンティティ
 * </pre>
 */
@Data
@Entity
@Table(name="ApplyChat")
@IdClass(ApplyChatId.class)
public class ApplyChat implements Serializable {
	
	/** serialVersionUID */
	private static final long serialVersionUID = 6110613325031538565L;

	/** 募集ID */
	@Id
	@JsonProperty(value = "RoundSerialNo")
	private Integer roundSerialNo;

	/** 会員ID */
	@JsonProperty(value = "MemberID")
	private String memberId;

	/** チャット内容 */
	@JsonProperty(value = "ChatContents")
	private String chatContents;

	/** 表示順 */
	@Id
	@JsonProperty(value = "SortOrder")
	private Integer sortOrder;

	/** 登録日時 */
	@JsonProperty(value = "RegDate")
	private String regDate;

	/** 会員情報 */
	@JsonIgnore
	@OneToOne
	@JoinColumn(name = "memberID")
	private MemberInfo memberInfo;
}
