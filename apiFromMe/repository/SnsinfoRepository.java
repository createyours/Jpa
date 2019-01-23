package org.leadingsoft.golf.api.repository;

import org.leadingsoft.golf.api.entity.SnsInfo;

public interface SnsinfoRepository extends BaseRepository<SnsInfo,Long> {
	SnsInfo findBySNSID(String SNSID);

}
