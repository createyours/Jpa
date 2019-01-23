//****************************************************************************//
// システム         : Golf
//----------------------------------------------------------------------------//
//                (c)Copyright 2018 LeadingSoft All rights reserved.
//============================================================================//
package org.leadingsoft.golf.api.repository;

import java.util.List;

import org.leadingsoft.golf.api.entity.RakutenGolfCourseInfo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * <pre>
 * 楽天ゴルフ場基本情報リポジトリ
 * </pre>
 */
public interface RakutenGolfCourseInfoRepository extends JpaRepository<RakutenGolfCourseInfo, String> {

	/**
	 * ゴルフ場名(略称)で楽天ゴルフ場基本情報を取得する
	 * 
	 * @param golfCourseAbbr ゴルフ場名(略称)
	 * 
	 * @return 楽天ゴルフ場基本情報リスト
	 */
	List<RakutenGolfCourseInfo> findByGolfCourseAbbrContaining(String golfCourseAbbr);
}
