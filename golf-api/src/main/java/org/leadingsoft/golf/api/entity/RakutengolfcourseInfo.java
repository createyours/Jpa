package org.leadingsoft.golf.api.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
*  楽天ゴルフ場基本情報
* @author 大狼狗 2019-01-18
*/
@Data
@Entity
@Table(name="rakutengolfcourseinfo")
public class RakutengolfcourseInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue
    /**
    * ゴルフ場id
    */
    private String golfcourseid;

    /**
    * ゴルフ場名
    */
    private String golfcoursename;

    /**
    * ゴルフ場名(略称)
    */
    private String golfcourseabbr;

    /**
    * かな名
    */
    private String namek;

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
