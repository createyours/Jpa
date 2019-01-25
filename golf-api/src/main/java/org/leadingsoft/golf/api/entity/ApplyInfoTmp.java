package org.leadingsoft.golf.api.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "ApplyInfoTmp")
public class ApplyInfoTmp implements Serializable {
	
	private static final long serialVersionUID = -4614414188962090338L;
	
	
	@Id
	String apovCny;
	String roundSerialNo;
}
