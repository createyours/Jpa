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
 * チャットIDエンティティ
 * </pre>
 */
@Data
public class ApplyChatId implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = -1343120079491385985L;

	/** 募集ID */
	private Integer roundSerialNo;

	/** 表示順 */
	private Integer sortOrder;

	@Override
	public int hashCode() {
		int result = 1;
		result = roundSerialNo.hashCode() + sortOrder.hashCode();
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
		final ApplyChatId other = (ApplyChatId) obj;
		if (other.getRoundSerialNo().equals(this.roundSerialNo) && other.getSortOrder().equals(this.sortOrder)) {
			return true;
		}
		return false;
	}
}
