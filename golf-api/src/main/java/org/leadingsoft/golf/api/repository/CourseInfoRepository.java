package org.leadingsoft.golf.api.repository;

import org.leadingsoft.golf.api.entity.CourseInfo;

public interface CourseInfoRepository extends BaseRepository<CourseInfo, Long> {
	CourseInfo findById(String ID);
}
