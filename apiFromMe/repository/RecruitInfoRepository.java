package org.leadingsoft.golf.api.repository;

import java.util.List;
import java.util.Map;

import org.leadingsoft.golf.api.entity.RecruitInfo;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface RecruitInfoRepository extends BaseRepository<RecruitInfo, Long>,JpaSpecificationExecutor<RecruitInfo> {
	RecruitInfo findByRoundSerialNo(Integer roundSerialNo);
	
	boolean existsByRoundSerialNo(Integer roundSerialNo);

	@Query(value = "select (RecruitInfo.RecruitNum - IFNULL(Apply.ApovCnt, 0)) AS count from RecruitInfo left join "
			+ "(select count(0) as ApovCnt, RoundSerialNo from ApplyInfo where ApovStatus = '1' group by RoundSerialNo) Apply"
			+ " ON Apply.RoundSerialNo = RecruitInfo.RoundSerialNo where RecruitInfo.RoundSerialNo = :roundSerialNo", nativeQuery = true)
	List<Integer> getCountByRoundSerialNo(@Param("roundSerialNo") Integer roundSerialNo);
	
	@Transactional
	@Modifying
	@Query(value = "update RecruitInfo set Status = :CollectStateCode_COMPLETE_code where RoundSerialNo= :roundSerialNo")
	int updateRecruitInfo(@Param("CollectStateCode_COMPLETE_code") Integer CollectStateCode_COMPLETE_code,@Param("roundSerialNo") Integer roundSerialNo);
	
	@Transactional
	@Modifying
	@Query(value = "select RecruitInfo.*,CourseInfo.GolfCourseName, CourseInfo.`Name` AS CourseName , '' as ApovStatus , APOVCnt.ApovCnt from RecruitInfo"
			+ " INNER JOIN CourseInfo ON CourseInfo.ID = RecruitInfo.CourseID"
			+ " LEFT JOIN (select count(0) as ApovCnt, RoundSerialNo from ApplyInfo where ApovStatus = :ApovStatusCode_ApovOK_code group by RoundSerialNo) APOVCnt"
			+ " ON APOVCnt.RoundSerialNo =RecruitInfo.RoundSerialNo"
			+ " where RecruitInfo.MemberID = :memberId and RecruitInfo.DelFlg = '0' "
			+ "and  RecruitInfo.PlayDate >= :playDate and (RecruitInfo.Status  is null OR RecruitInfo.Status != :CollectStateCode_DELETE_code)",nativeQuery = true)
	List<Map<String,Object>> findRecruitInfoAtCollectFlgEqualUNMATCHED(@Param("ApovStatusCode_ApovOK_code") String ApovStatusCode_ApovOK_code,@Param("memberId") String memberId,@Param("playDate") String playDate,@Param("CollectStateCode_DELETE_code") Integer CollectStateCode_DELETE_code);
	
	
	@Transactional
	@Modifying
	@Query(value = "select RecruitInfo.*,CourseInfo.GolfCourseName, CourseInfo.`Name` AS CourseName , ApplyInfo.ApovStatus , APOVCnt.ApovCnt from RecruitInfo"
			+ " INNER JOIN CourseInfo ON CourseInfo.ID = RecruitInfo.CourseID"
			+ " INNER JOIN ApplyInfo ON ApplyInfo.RoundSerialNo = RecruitInfo.RoundSerialNo AND ApplyInfo.MemberID = :memberId"
			+ " LEFT JOIN (select count(0) as ApovCnt, RoundSerialNo from ApplyInfo where ApovStatus = :ApovStatusCode_ApovOK_code group by RoundSerialNo) APOVCnt"
			+ " ON APOVCnt.RoundSerialNo = RecruitInfo.RoundSerialNo"
			+ " where RecruitInfo.DelFlg = '0' and RecruitInfo.PlayDate >= :playDate and (RecruitInfo.Status  is null OR RecruitInfo.Status != :CollectStateCode_DELETE_code)",nativeQuery = true)
	List<Map<String,Object>> findRecruitInfoAtCollectFlgNOTEqualUNMATCHED(@Param("ApovStatusCode_ApovOK_code") String ApovStatusCode_ApovOK_code,@Param("memberId") String memberId,@Param("playDate") String playDate,@Param("CollectStateCode_DELETE_code") Integer CollectStateCode_DELETE_code);
	
	
	
	
	/*
	//TODO CollectService:serach，@query查询方式
	@Transactional
	@Modifying
	@Query(value = "select RecruitInfo.*,CourseInfo.GolfCourseName, CourseInfo.`Name` AS CourseName , ApplyInfo.ApovStatus , APOVCnt.ApovCnt from RecruitInfo"
			+ " INNER JOIN CourseInfo ON CourseInfo.ID = RecruitInfo.CourseID"
			+ " LEFT JOIN ApplyInfo ON ApplyInfo.RoundSerialNo = RecruitInfo.RoundSerialNo AND  ApplyInfo.MemberID = :memberId"
			+ " LEFT JOIN (select count(0) as ApovCnt, RoundSerialNo from ApplyInfo where ApovStatus = :ApovStatusCode_ApovOK_code group by RoundSerialNo) APOVCnt"
			+ " ON APOVCnt.RoundSerialNo = RecruitInfo.RoundSerialNo where RecruitInfo.DelFlg = '0' and PlayDate = :playDate and RecruitInfo.RoundSerialNo= :roundSerialNo"
			+ " and (RecruitInfo.Status is null OR  RecruitInfo.Status != :CollectStateCode_DELETE_code)",nativeQuery = true)
	List<Map<String,Object>> findRecruitInfoAtRoundSerialNoIsNotEmpty(@Param("memberId") String memberId,@Param("ApovStatusCode_ApovOK_code") String ApovStatusCode_ApovOK_code,
			@Param("playDate") String playDate,@Param("roundSerialNo") Integer roundSerialNo,@Param("CollectStateCode_DELETE_code") String CollectStateCode_DELETE_code);
	
	
	@Transactional
	@Modifying
	@Query(value = "select RecruitInfo.*,CourseInfo.GolfCourseName, CourseInfo.`Name` AS CourseName , ApplyInfo.ApovStatus , APOVCnt.ApovCnt from RecruitInfo"
			+ " INNER JOIN CourseInfo ON CourseInfo.ID = RecruitInfo.CourseID"
			+ " LEFT JOIN ApplyInfo ON ApplyInfo.RoundSerialNo =RecruitInfo.RoundSerialNo AND  ApplyInfo.MemberID = :memberId"
			+ " LEFT JOIN (select count(0) as ApovCnt, RoundSerialNo from ApplyInfo where ApovStatus = :ApovStatusCode_ApovOK_code group by RoundSerialNo) APOVCnt"
			+ " ON APOVCnt.RoundSerialNo =RecruitInfo.RoundSerialNo where RecruitInfo.DelFlg = '0' and PlayDate= :playDate and RecruitInfo.MemberID != :memberId1"
			+ " and (RecruitInfo.Status is null OR RecruitInfo.Status != :CollectStateCode_DELETE_code)",nativeQuery = true)
	List<Map<String,Object>> findRecruitInfoAtMemberIdIsNotEmpty(@Param("memberId") String memberId,@Param("ApovStatusCode_ApovOK_code") String ApovStatusCode_ApovOK_code,
			@Param("playDate") String playDate,@Param("memberId1") String memberId1,@Param("CollectStateCode_DELETE_code") String CollectStateCode_DELETE_code);*/
}
		
        
        
