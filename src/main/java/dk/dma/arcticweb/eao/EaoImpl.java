package dk.dma.arcticweb.eao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.google.inject.persist.Transactional;

import dk.dma.arcticweb.domain.IEntity;

@Transactional
public class EaoImpl implements Eao {

	@Inject
	protected EntityManager em;

	@Override
	@Transactional
	public IEntity getByPrimaryKey(Class<? extends IEntity> clazz, Object id) {
		return em.find(clazz, id);
	}

	@Override
	@Transactional
	public void remove(IEntity entity) {
		em.remove(em.merge(entity));
	}

	@Override
	@Transactional
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

	public static IEntity getSingleOrNull(List<? extends IEntity> list) {
		return (list == null || list.size() == 0) ? null : list.get(0);
	}

}
