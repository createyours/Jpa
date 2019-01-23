package org.leadingsoft.golf.api.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
*  楽天ゴルフ場基本情報
* @author 大狼狗 2019-01-18
*/
@Entity
@Table(name="rakutengolfcourseinfo")
public class Rakutengolfcourseinfo implements Serializable {
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

    public String getGolfcourseid() {
		return golfcourseid;
	}

	public void setGolfcourseid(String golfcourseid) {
		this.golfcourseid = golfcourseid;
	}

	public String getGolfcoursename() {
		return golfcoursename;
	}

	public void setGolfcoursename(String golfcoursename) {
		this.golfcoursename = golfcoursename;
	}

	public String getGolfcourseabbr() {
		return golfcourseabbr;
	}

	public void setGolfcourseabbr(String golfcourseabbr) {
		this.golfcourseabbr = golfcourseabbr;
	}

	public String getNamek() {
		return namek;
	}

	public void setNamek(String namek) {
		this.namek = namek;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public float getPoint1() {
		return point1;
	}

	public void setPoint1(float point1) {
		this.point1 = point1;
	}

	public float getPoint2() {
		return point2;
	}

	public void setPoint2(float point2) {
		this.point2 = point2;
	}

	public float getPoint3() {
		return point3;
	}

	public void setPoint3(float point3) {
		this.point3 = point3;
	}

	public String getOther() {
		return other;
	}

	public void setOther(String other) {
		this.other = other;
	}

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
