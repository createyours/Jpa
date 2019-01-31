package org.leadingsoft.golf.api.repository;

import org.leadingsoft.golf.api.entity.ApplyChat;
import org.leadingsoft.golf.api.entity.ApplyChatPK;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ApplyChatRepository extends BaseRepository<ApplyChat,ApplyChatPK>, JpaSpecificationExecutor<ApplyChat> {
	int countByRoundSerialNo(int roundserialno);
}
