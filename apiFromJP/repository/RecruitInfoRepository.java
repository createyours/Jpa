//****************************************************************************//
// システム         : Golf
//----------------------------------------------------------------------------//
//                (c)Copyright 2018 LeadingSoft All rights reserved.
//============================================================================//
package org.leadingsoft.golf.api.repository;

import org.leadingsoft.golf.api.entity.RecruitInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * <pre>
 * 募集情報リポジトリ
 * </pre>
 */
public interface RecruitInfoRepository extends JpaRepository<RecruitInfo, Integer> {

	/**
	 * 指定募集IDの募集情報存在するチェック
	 * 
	 * @param roundSerialNo 募集ID
	 * @return 存在フラグ
	 */
	boolean existsByRoundSerialNo(int roundSerialNo);

	/**
	 * 指定募集IDの募集情報を更新する
	 * 
	 * @param roundSerialNo 募集ID
	 */
	@Transactional
	@Modifying
	@Query("update RecruitInfo set delFlg = :delFlg  where RoundSerialNo = :roundSerialNo")
	void updateDelFlg(@Param("delFlg") int delFlg, @Param("roundSerialNo") int roundSerialNo);

	/**
	 * 指定募集IDの募集情報を更新する
	 * 
	 * @param roundSerialNo 募集ID
	 */
	RecruitInfo findByRoundSerialNo(Integer roundSerialNo);
}
