package org.leadingsoft.golf.api.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "temporary")
@IdClass(TemporaryPK.class)
public class Temporary implements Serializable {
	
	private static final long serialVersionUID = -4786737773516486980L;

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

}
