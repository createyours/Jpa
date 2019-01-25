package org.leadingsoft.golf.api.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;


/**
*  チャット
*/
@Entity
@Data
@Table(name="applychat")
@IdClass(ApplyChatPK.class)
public class ApplyChat implements Serializable {

	private static final long serialVersionUID = 4668619139416238507L;
		@Id
	    @GeneratedValue(strategy=GenerationType.AUTO)
	    /**
	    * 募集id
	    */
	    private int roundserialno;
		/**
	    * 会員id
	    */
	    private String memberid;
	    /**
	    * チャット内容
	    */
	    private String chatcontents;
	    /**
	    * 表示順
	    */
	    @Id
	    @GeneratedValue
	    private int sortorder;
	    /**
	    * 登録日時
	    */
	    private String regdate;
	    
	    @OneToOne
		@JoinColumn(name = "memberID")
		private MemberInfo memberInfo;
	}

