package org.leadingsoft.golf.api.entity;

import java.io.Serializable;

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
@Data
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
    
    @OneToOne
	@JoinColumn(name = "memberID")
	private MemberInfo memberInfo;
}
