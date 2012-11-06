package dk.dma.arcticweb.ejb.eao;

import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import dk.dma.arcticweb.ejb.domain.IEntity;

@Stateless
public class EaoImpl implements Eao {

	@PersistenceContext(unitName = "arcticweb")
	protected EntityManager em;

	@Override
	public IEntity getByPrimaryKey(Class<? extends IEntity> clazz, Object id) {
		return em.find(clazz, id);
	}

	@Override
	public List<? extends IEntity> getAll(Class<? extends IEntity> clazz) {
		Query query = em.createNamedQuery(clazz.getSimpleName() + ":getAll");
		@SuppressWarnings("unchecked")
		List<IEntity> list = query.getResultList();
		return list;
	}

	@Override
	public void remove(IEntity entity) {
		em.remove(em.merge(entity));
	}

	@Override
	public IEntity saveEntity(IEntity entity) {
		if (entity.isPersisted()) {
			// Update existing
			entity = em.merge(entity);
		} else {
			// Save new
			em.persist(entity);
		}
		return entity;
	}

	@Override
	public List<? extends IEntity> getByQuery(String queryStr, Map<String, Object> params) {
		Query query = em.createNamedQuery(queryStr);
		return getList(query, params);
	}
	
	@Override
	public List<? extends IEntity> getByNamedQuery(String namedQuery, Map<String, Object> params) {
		Query query = em.createNamedQuery(namedQuery);
		return getList(query, params);		
	}
	
	private List<? extends IEntity> getList(Query query, Map<String, Object> params) {
		for (String key : params.keySet()) {
			query.setParameter(key, params.get(key));
		}
		@SuppressWarnings("unchecked")
		List<IEntity> list = query.getResultList();
		return list;
	}
	
	public static IEntity getSingleOrNull(List<? extends IEntity> list) {
		return (list == null || list.size() == 0) ? null : list.get(0);
	}

}
