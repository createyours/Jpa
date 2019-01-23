//****************************************************************************//
// システム         : Golf
//----------------------------------------------------------------------------//
//                (c)Copyright 2018 LeadingSoft All rights reserved.
//============================================================================//
package org.leadingsoft.golf.api.entity;

import java.io.Serializable;

import lombok.Data;

/**
 * <pre>
 * SNS情報ID
 * </pre>
 */
@Data
public class SNSInfoId implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = -3240859325591619509L;

	/** 会員ID */
	private String memberID;

	/** SNSID */
	private String sNSID;

	/** タイプ:FB, LINE */
	private Integer sNSType;

	@Override
	public int hashCode() {
		int result = 1;
		result = memberID.hashCode() + sNSID.hashCode() + sNSType.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final SNSInfoId other = (SNSInfoId) obj;
		if (other.getMemberID().equals(this.memberID) && other.getSNSID().equals(this.sNSID)
				&& other.getSNSType().equals(this.sNSType)) {
			return true;
		}
		return false;
	}
}
