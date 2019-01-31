package org.leadingsoft.golf.api.repository;

import org.leadingsoft.golf.api.entity.CourseInfo;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface CourseInfoRepository extends BaseRepository<CourseInfo, Long> {
	CourseInfo findById(String ID);
	
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
