package org.leadingsoft.golf.api.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.Data;


@Entity
@Table(name = "Snsinfo")
@IdClass(SnsInfoPK.class)
public class SnsInfo implements Serializable{
	/**
	 * 
	 */
	@Id
	private String memberID;
	
	@Id
	private String sNSID;
	
	@Id
	private int sNSType;
	private int authFlag;

	
	public SnsInfo( ){}

	
	public String getMemberID() {
		return memberID;
	}


	public void setMemberID(String memberID) {
		this.memberID = memberID;
	}

	public String getSNSID() {
		return sNSID;
	}


	public void setSNSID(String sNSID) {
		this.sNSID = sNSID;
	}

	public int getSNSType() {
		return sNSType;
	}


	public void setSNSType(int sNSType) {
		this.sNSType = sNSType;
	}


	public int getAuthFlag() {
		return authFlag;
	}


	public void setAuthFlag(int authFlag) {
		this.authFlag = authFlag;
	}
	
	
}
