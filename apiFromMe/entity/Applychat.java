package org.leadingsoft.golf.api.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;


/**
*  チャット
*/
@Entity
@Table(name="applychat")
@IdClass(ApplychatPK.class)
public class Applychat implements Serializable {

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
	    
	    @OneToOne(targetEntity=MemberInfo.class,fetch=FetchType.LAZY,cascade=CascadeType.ALL)
	    @JoinColumn(name="memberinfo_memberID")
	    private MemberInfo memberInfo;//hibernate 会自动生成一张中间表tbl_applychat_memberinfos
	    
	    
	    
	    public MemberInfo getMemberInfo() {
			return memberInfo;
		}
		public void setMemberInfo(MemberInfo memberInfo) {
			this.memberInfo = memberInfo;
		}
		public int getRoundserialno() {
			return roundserialno;
		}
		public void setRoundserialno(int roundserialno) {
			this.roundserialno = roundserialno;
		}
		public String getMemberid() {
			return memberid;
		}
		public void setMemberid(String memberid) {
			this.memberid = memberid;
		}
		public String getChatcontents() {
			return chatcontents;
		}
		public void setChatcontents(String chatcontents) {
			this.chatcontents = chatcontents;
		}
		public int getSortorder() {
			return sortorder;
		}
		public void setSortorder(int sortorder) {
			this.sortorder = sortorder;
		}
		public String getRegdate() {
			return regdate;
		}
		public void setRegdate(String regdate) {
			this.regdate = regdate;
		}

	}

