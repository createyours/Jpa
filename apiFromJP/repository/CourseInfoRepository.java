//****************************************************************************//
// システム         : Golf
//----------------------------------------------------------------------------//
//                (c)Copyright 2018 LeadingSoft All rights reserved.
//============================================================================//
package org.leadingsoft.golf.api.repository;

import org.leadingsoft.golf.api.entity.CourseInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * <pre>
 * コース情報リポジトリ
 * </pre>
 */
public interface CourseInfoRepository extends JpaRepository<CourseInfo, String> {

	/**
	 * 指定IDのコース情報を更新する
	 * 
	 * @param name           コース名
	 * @param golfCourseName ゴルフコース名
	 * @param id             ID
	 */
	@Transactional
	@Modifying
	@Query("UPDATE CourseInfo SET Name = :name, GolfCourseName = :golfCourseName where ID = :Id")
	void updateCourseInfo(@Param("name") String name, @Param("golfCourseName") String golfCourseName,
			@Param("Id") String id);
}
