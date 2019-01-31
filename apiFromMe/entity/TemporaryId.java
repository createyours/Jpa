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
 * 一時テーブルID
 * </pre>
 */
@Data
public class TemporaryId implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 7078366006651960575L;

	/** ID */
	private String iD;

	/** タイプ */
	private String type;

	@Override
	public int hashCode() {
		int result = 1;
		result = iD.hashCode() + type.hashCode();
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
		final TemporaryId other = (TemporaryId) obj;
		if (other.getID().equals(this.iD) && other.getType().equals(this.type)) {
			return true;
		}
		return false;
	}
}
