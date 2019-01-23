//****************************************************************************//
// システム         : Golf
//----------------------------------------------------------------------------//
//                (c)Copyright 2018 LeadingSoft All rights reserved.
//============================================================================//
package org.leadingsoft.golf.api.repository;

import java.util.List;

import org.leadingsoft.golf.api.entity.SNSInfo;
import org.leadingsoft.golf.api.entity.SNSInfoId;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * <pre>
 * SNS情報リポジトリ
 * </pre>
 */
public interface SNSInfoRepository extends JpaRepository<SNSInfo, SNSInfoId> {

	/**
	 * SNSIDでSNS情報を取得する
	 * 
	 * @param snsId SNSID
	 * @return SNS情報リスト
	 */
	List<SNSInfo> findBySNSID(String snsId);
}
