package org.leadingsoft.golf.api.repository;

import java.util.List;

import org.leadingsoft.golf.api.entity.ApplyInfoPK;
import org.leadingsoft.golf.api.entity.Applyinfo;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ApplyInfoRepository extends BaseRepository<Applyinfo,ApplyInfoPK>,JpaSpecificationExecutor<Applyinfo>{
	List<Applyinfo> findByRegNo(int regno);
	void deleteByRoundSerialNoAndRegNo(int roundserialno,int regno);
	
	boolean existsByRoundSerialNoAndRegNoAndApovStatus(Integer roundSerialNo,Integer regNo,String apovStatus);
	
	@Transactional
	@Modifying
	@Query("update Applyinfo set ApovStatus = :ApovStatusCode_ApovOK_code where RoundSerialNo= :roundSerialNo AND RegNo = :regNo")
	int updateApplyInfo(@Param("ApovStatusCode_ApovOK_code")String ApovStatusCode_ApovOK_code,@Param("roundSerialNo") Integer roundSerialNo,@Param("regNo") Integer regNo);
	
	
	List<Applyinfo> findByMemberIdAndRoundSerialNoAndApovStatus(String memberId,Integer roundSerialNo,String apovStatus);
	
	@Modifying
	@Transactional
	void deleteByRoundSerialNoAndRegNo(Integer roundSerialNo,Integer regNo);
}
