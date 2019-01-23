package org.leadingsoft.golf.api.repository;

import org.leadingsoft.golf.api.entity.Applychat;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ApplychatRepository extends BaseRepository<Applychat,Long>, JpaSpecificationExecutor<Applychat> {
	int countByRoundserialno(int roundserialno);
}
