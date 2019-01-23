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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

/**
*  応募情報
*/
@Entity
@Table(name="applyinfo")
@IdClass(ApplyInfoPK.class)
public class Applyinfo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    /**
    * 募集id
    */
    private int roundserialno;
    
    @OneToOne(targetEntity=MemberInfo.class,fetch=FetchType.EAGER,cascade=CascadeType.ALL)
    @JoinColumn(name="memberID")//private MemberInfo memberInfo 的memberInfo.memberID
    private MemberInfo memberInfo;
    /**
    * 応募者id
    */
    private String memberid;


	@Id
    @GeneratedValue
    /**
    * 受付通番
    */
    private int regno;

    /**
    * s varchar(256)
    */
    private String comments;

    /**
    * キャンセルフラグ
    */
    private String cancelflag;

    /**
    * キャンセル待ちフラグ
    */
    private String waitflag;

    /**
    * チャット内容:応募者と主催者の間でチャットする内容（チャットを提供してから使う予備フィールド）
    */
    private String chatcontents;
    
    /**
    * 承認ステータス
    */
    private String apovstatus;
    
    public MemberInfo getMemberInfos() {
		return memberInfo;
	}

	public void setMemberInfos(MemberInfo memberInfo) {
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

	public int getRegno() {
		return regno;
	}

	public void setRegno(int regno) {
		this.regno = regno;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getCancelflag() {
		return cancelflag;
	}

	public void setCancelflag(String cancelflag) {
		this.cancelflag = cancelflag;
	}

	public String getWaitflag() {
		return waitflag;
	}

	public void setWaitflag(String waitflag) {
		this.waitflag = waitflag;
	}

	public String getChatcontents() {
		return chatcontents;
	}

	public void setChatcontents(String chatcontents) {
		this.chatcontents = chatcontents;
	}

	public String getApovstatus() {
		return apovstatus;
	}

	public void setApovstatus(String apovstatus) {
		this.apovstatus = apovstatus;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
