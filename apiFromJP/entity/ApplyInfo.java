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
 * 応募情報エンティティ
 * </pre>
 */
@Data
@Entity
@Table(name="ApplyInfo")
@IdClass(ApplyInfoId.class)
public class ApplyInfo implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = -1525115654770978237L;

	/** 募集ID */
	@Id
	@JsonProperty(value = "RoundSerialNo")
	private Integer roundSerialNo;

	/** 応募者ID */
	@JsonProperty(value = "MemberID")
	private String memberId;

	/** 受付通番 */
	@Id
	@JsonProperty(value = "RegNo")
	private Integer regNo;

	/** 主催者へのコメント */
	@JsonProperty(value = "Comments")
	private String comments;

	/** キャンセルフラグ */
	@JsonProperty(value = "CancelFlag")
	private String cancelFlag;

	/** キャンセル待ちフラグ */
	@JsonProperty(value = "WaitFlag")
	private String waitFlag;

	/** チャット内容 */
	@JsonProperty(value = "ChatContents")
	private String chatContents;

	/** 承認ステータス */
	@JsonProperty(value = "ApovStatus")
	private String apovStatus;

	/** 会員情報 */
	@JsonIgnore
	@OneToOne
	@JoinColumn(name = "memberID")
	private MemberInfo memberInfo;
}
