//****************************************************************************//
// システム         : Golf
//----------------------------------------------------------------------------//
//                (c)Copyright 2018 LeadingSoft All rights reserved.
//============================================================================//
package org.leadingsoft.golf.api.repository;

import java.util.List;

import org.leadingsoft.golf.api.entity.Temporary;
import org.leadingsoft.golf.api.entity.TemporaryId;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * <pre>
 * 一時テーブルリポジトリ
 * </pre>
 */
public interface TemporaryRepository extends JpaRepository<Temporary, TemporaryId> {

	/**
	 * 一時テーブル情報を取得する
	 * 
	 * @param id ID
	 * @return 一時テーブル情報リスト
	 */
	List<Temporary> findByID(String id);
}
