//****************************************************************************//
// システム         : Golf
//----------------------------------------------------------------------------//
//                (c)Copyright 2018 LeadingSoft All rights reserved.
//============================================================================//
package org.leadingsoft.golf.api.repository;

import java.util.List;

import org.leadingsoft.golf.api.entity.MemberInfo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * <pre>
 * 会員情報リポジトリ
 * </pre>
 */
public interface MemberInfoRepository extends JpaRepository<MemberInfo, String> {

	/**
	 * 氏名またはメールアドレスが一致する会員情報取得処理
	 * @param name 氏名
	 * @param email メールアドレス
	 * 
	 * @return 会員情報リスト
	 */
	List<MemberInfo> findByNameOrEmail(String name, String email);

	/**
	 * 指定メールアドレスの存在チェック
	 * @param email メールアドレス
	 * 
	 * @return 該当メールアドレス存在かどうか
	 */
	boolean existsByEmail(String email);


	/**
	 * 指定会員IDの存在チェック
	 * @param memberID 会員ID
	 * 
	 * @return 会員情報
	 */
	List<MemberInfo> findByMemberID(String memberID);
}
