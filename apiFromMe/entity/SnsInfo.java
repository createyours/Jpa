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
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * <pre>
 * SNS情報エンティティ
 * </pre>
 */
@Data
@Entity
@Table(name = "SNSInfo")
@IdClass(SNSInfoPK.class)
public class SNSInfo implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = -5377357233906609264L;

	/** 会員ID */
	@Id
	@JsonProperty(value = "MemberID")
	private String memberID;

	/** SNSID */
	@JsonProperty(value = "SNSID")
	private String sNSID;
	
	/** タイプ:FB, LINE */
	@JsonProperty(value = "SNSType")
	private Integer sNSType;

	/** 認証フラグ */
	@JsonProperty(value = "AuthFlag")
	private Integer authFlag;

}
