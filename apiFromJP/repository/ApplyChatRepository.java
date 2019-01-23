//****************************************************************************//
// システム         : Golf
//----------------------------------------------------------------------------//
//                (c)Copyright 2018 LeadingSoft All rights reserved.
//============================================================================//
package org.leadingsoft.golf.api.repository;

import java.util.List;
import java.util.Map;

import org.leadingsoft.golf.api.entity.ApplyChat;
import org.leadingsoft.golf.api.entity.ApplyChatId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * <pre>
 * チャットリポジトリ
 * </pre>
 */
public interface ApplyChatRepository
		extends JpaRepository<ApplyChat, ApplyChatId>, JpaSpecificationExecutor<ApplyChat> {

	/**
	 * 指定募集IDのチャット情報の存在チェック
	 * 
	 * @param roundSerialNo 募集ID
	 * 
	 * @return チャット情報存在フラグ
	 */
	long countByRoundSerialNo(int roundSerialNo);

	/**
	 * 指定募集IDのチャット情報の存在チェック
	 * 
	 * @param memberId 会員ID
	 * 
	 * @return 検索結果リスト
	 */
	@Query(value = "select ApplyChat.RoundSerialNo, ApplyChat.MemberID, max(ApplyChat.SortOrder) as maxSortOrder, CourseInfo.GolfCourseName, RecruitInfo.PlayDate from ApplyChat inner join RecruitInfo on ApplyChat.RoundSerialNo = RecruitInfo.RoundSerialNo inner join CourseInfo on CourseInfo.ID = RecruitInfo.CourseID where ApplyChat.RoundSerialNo in (select distinct(RoundSerialNo) from ApplyChat where MemberID = :memberId) group by ApplyChat.RoundSerialNo", nativeQuery = true)
	List<Map<String, Object>> getApplyInfoByMemberId(@Param("memberId") String memberId);
}
