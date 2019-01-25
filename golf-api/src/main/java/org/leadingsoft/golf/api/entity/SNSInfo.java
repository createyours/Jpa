package org.leadingsoft.golf.api.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "Snsinfo")
@IdClass(SNSInfoPK.class)
public class SNSInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6468553098348466612L;

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
	
}
