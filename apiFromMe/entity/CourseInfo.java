package org.leadingsoft.golf.api.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
*  コース情報:ゴルフ場基本情報
*
*/
@Entity
@Data
@Table(name="courseinfo")
public class CourseInfo implements Serializable{

    private static final long serialVersionUID = 1L;
    @Id
    /**
    * コースid
    */
    private String iD;

    /**
    * コース名
    */
    private String name;

    /**
    * かな名
    */
    private String name_k;

    /**
    * 英語名
    */
    private String name_e;

    /**
    * golfcoursename
    */
    private String golfcoursename;

    /**
    * 都道府県
    */
    private String state;

    /**
    * 住所
    */
    private String address;

    /**
    * 電話番号
    */
    private String tel;

    /**
    * 経度
    */
    private float longitude;

    /**
    * 緯度
    */
    private float latitude;

    /**
    * 説明
    */
    private String caption;

    /**
    * コースタイプ:山岳、丘陵、林間など
    */
    private int type;

    /**
    * 評価１:楽天評価
    */
    private float point1;

    /**
    * 評価２:gdo評価
    */
    private float point2;

    /**
    * 評価３:その他評価
    */
    private float point3;

    /**
    * その他
    */
    private String other;
}
