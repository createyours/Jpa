//****************************************************************************//
// システム         : Golf
//----------------------------------------------------------------------------//
//                (c)Copyright 2018 LeadingSoft All rights reserved.
//============================================================================//
package org.leadingsoft.golf.api.repository;

import java.util.List;

import org.leadingsoft.golf.api.entity.ApplyInfo;
import org.leadingsoft.golf.api.entity.ApplyInfoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * <pre>
 * 応募情報リポジトリ
 * </pre>
 */
public interface ApplyInfoRepository extends JpaRepository<ApplyInfo, ApplyInfoId>, JpaSpecificationExecutor<ApplyInfo> {

	/**
	 * 指定会員ID、募集ID、承認ステータスの募集情報の存在チェック
	 * 
	 * @param memberId      会員ID
	 * @param RoundSerialNo 募集ID
	 * @param apovStatusLst 承認ステータスリスト
	 * 
	 * @return 募集情報存在フラグ
	 */
	boolean existsByMemberIdAndRoundSerialNoAndApovStatusIn(String memberId, int RoundSerialNo,
			List<String> apovStatusLst);

	/**
	 * 指定受付通番の会員情報の存在チェック
	 * 
	 * @param regNo 受付通番
	 * 
	 * @return 会員情報存在フラグ
	 */
	boolean existsByRegNo(int regNo);

	/**
	 * 指定会員ID、募集ID、承認ステータスの募集情報の存在チェック
	 * 
	 * @param memberId      会員ID
	 * @param RoundSerialNo 募集ID
	 * @param apovStatusLst 承認ステータスリスト
	 * 
	 * @return 募集情報存在フラグ
	 */
	List<ApplyInfo> findByMemberIdAndRoundSerialNoAndApovStatus(String memberId, int RoundSerialNo, String apovStatus);

}
