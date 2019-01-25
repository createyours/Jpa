package org.leadingsoft.golf.api.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;

import lombok.Data;

@Embeddable
@Data
public class TemporaryPK implements Serializable{

	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//ID
    private  String iD;
   
	 //タイプ
    private  String type;

    

	public TemporaryPK() {}
	
	public TemporaryPK(String iD, String type) {
		super();
		this.iD = iD;
		this.type = type;
	}
	
	
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((iD == null) ? 0 : iD.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		TemporaryPK other = (TemporaryPK) obj;
		if (iD == null) {
			if (other.iD != null)
				return false;
		} else if (!iD.equals(other.iD))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}


    
   
}
