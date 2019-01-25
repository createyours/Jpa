package org.leadingsoft.golf.api.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;

import lombok.Data;

@Embeddable
@Data
public class SNSInfoPK implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String memberID;
	private String sNSID;
	private int sNSType;
	
	
	public SNSInfoPK(){}
	public SNSInfoPK(String memberID, String sNSID, int sNSType) {
		super();
		this.memberID = memberID;
		this.sNSID = sNSID;
		this.sNSType = sNSType;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((memberID == null) ? 0 : memberID.hashCode());
		result = prime * result + ((sNSID == null) ? 0 : sNSID.hashCode());
		result = prime * result + sNSType;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SNSInfoPK other = (SNSInfoPK) obj;
		if (memberID == null) {
			if (other.memberID != null)
				return false;
		} else if (!memberID.equals(other.memberID))
			return false;
		if (sNSID == null) {
			if (other.sNSID != null)
				return false;
		} else if (!sNSID.equals(other.sNSID))
			return false;
		if (sNSType != other.sNSType)
			return false;
		return true;
	}
	
}
