package org.leadingsoft.golf.api.repository;


import org.leadingsoft.golf.api.entity.Temporary;

public interface TemporaryRepository extends BaseRepository<Temporary,Long>{
	Temporary findByID(String ID);
}
