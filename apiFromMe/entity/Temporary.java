package org.leadingsoft.golf.api.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "temporary")
@IdClass(TemporaryPK.class)
public class Temporary implements Serializable {
	
	   
	 //ID
	  @Id
      private  String iD;
     
	 //タイプ
	  @Id
      private  String type;
     
	 //一時名
      private  String TemporaryName;
     
	 //一時値
      private  String TemporaryValue;
     
	 //作成日時
      private  String InsTstmp;
     
	 //有効期限
      private  String expire;

	public String getID() {
		return iD;
	}

	public void setID(String iD) {
		this.iD = iD;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTemporaryName() {
		return TemporaryName;
	}

	public void setTemporaryName(String temporaryName) {
		this.TemporaryName = temporaryName;
	}

	public String getTemporaryValue() {
		return TemporaryValue;
	}

	public void setTemporaryValue(String temporaryValue) {
		this.TemporaryValue = temporaryValue;
	}

	public String getInsTstmp() {
		return InsTstmp;
	}

	public void setInsTstmp(String insTstmp) {
		this.InsTstmp = insTstmp;
	}

	public String getExpire() {
		return expire;
	}

	public void setExpire(String expire) {
		this.expire = expire;
	}
     
      

	

}
