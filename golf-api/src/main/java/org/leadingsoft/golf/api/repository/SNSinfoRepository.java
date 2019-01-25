package org.leadingsoft.golf.api.repository;

import org.leadingsoft.golf.api.entity.SNSInfo;
import org.leadingsoft.golf.api.entity.SNSInfoPK;

public interface SNSinfoRepository extends BaseRepository<SNSInfo,SNSInfoPK> {
	SNSInfo findBySNSID(String SNSID);

}
