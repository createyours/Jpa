package org.leadingsoft.golf.api.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

public class BaseRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID>
    implements BaseRepository<T, ID> {
  @PersistenceContext
  private EntityManager entityManager;

  private Class<T> entityClass;

  public BaseRepositoryImpl(Class<T> domainClass, EntityManager entityManager) {
    super(domainClass, entityManager);
    this.entityManager = entityManager;
    this.entityClass = domainClass;
  }

  @Override
  public List<T> sqlQuery(String queryString, Object... values) {
    Session session = entityManager.unwrap(org.hibernate.Session.class);
    SQLQuery<T> query = session.createSQLQuery(queryString);
    if (values != null) {
      query.setProperties(values);
    }
    query.setResultTransformer(new BeanTransformerAdapter(this.entityClass));

    return query.list();
  }

  @Override
  public List sqlQuery(String queryString, Map<String, ?> values) {
    Session session = entityManager.unwrap(org.hibernate.Session.class);
    SQLQuery query = session.createSQLQuery(queryString);

    // //给条件赋上值
    // if (values != null && !values.isEmpty()) {
    // for (Map.Entry<String, ?> entry : values.entrySet()) {
    // query.setParameter(entry.getKey(), entry.getValue());
    // }
    // }

    if (values != null) {
      query.setProperties(values);
    }

    query.setResultTransformer(new BeanTransformerAdapter(this.entityClass));

    return query.list();
  }

}
