package org.leadingsoft.golf.api.repository;

import java.util.List;

import org.leadingsoft.golf.api.entity.MemberInfo;

public interface MemberInfoRepository extends BaseRepository<MemberInfo, Long> {
	
	//public MemberInfo findByName(String name);
	public List<MemberInfo> findByNameOrEmail(String name,String email);
	public List<MemberInfo> findByEmail(String email);
	public List<MemberInfo> findByMemberID(String memberID);
	
	
	
}
