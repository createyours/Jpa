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
 * 応募情報ID
 * </pre>
 */
@Data
public class ApplyInfoPK implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 1632395756382505186L;

	/** 募集ID */
	private Integer roundSerialNo;

	/** 受付通番 */
	private Integer regNo;

	@Override
	public int hashCode() {
		int result = 1;
		result = roundSerialNo.hashCode() + regNo.hashCode();
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
		final ApplyInfoPK other = (ApplyInfoPK) obj;
		if (other.getRoundSerialNo().equals(this.roundSerialNo) && other.getRegNo().equals(this.regNo)) {
			return true;
		}
		return false;
	}
}
