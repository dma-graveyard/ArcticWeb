package dk.dma.arcticweb.ejb.eao;

import java.util.List;
import java.util.Map;

import dk.dma.arcticweb.ejb.domain.IEntity;

public interface Eao {

	public IEntity getByPrimaryKey(Class<? extends IEntity> clazz, Object id);

	/**
	 * Get all entities of the type clazz. The entityBean must define a
	 * Namedquery <clazz.simpleName>:getAll
	 * 
	 * @param clazz
	 * @return List of IEntity
	 */
	public abstract List<? extends IEntity> getAll(Class<? extends IEntity> clazz);

	/**
	 * Delete the entity bean
	 * 
	 * @param bean
	 */
	public abstract void remove(IEntity bean);

	/**
	 * Save (insert or update) the entity bean
	 * 
	 * @param bean
	 * @return updated bean
	 */
	public abstract IEntity saveEntity(IEntity bean);

	/**
	 * Give query as argument
	 * @param queryStr
	 * @param params
	 * @return
	 */
	public List<? extends IEntity> getByQuery(String queryStr, Map<String, Object> params);
	
	/**
	 * Give named query as argument
	 * @param namedQuery
	 * @param params
	 * @return
	 */
	public List<? extends IEntity> getByNamedQuery(String namedQuery, Map<String, Object> params);
	
}
