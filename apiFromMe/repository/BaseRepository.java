package org.leadingsoft.golf.api.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean // skip for sping scan
public interface BaseRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {

  // データを検索
  public List<T> sqlQuery(String queryString, Object... values);

  public List<T> sqlQuery(String queryString, Map<String, ?> values);
}