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
	 * The entityBean must define a Namedquery
	 * '"<clazz.simpleName>:getByCriteria' with one parameter named 'param' ex :
	 * 
	 * @NamedQuery(name = "ClassName:getByCriteria", query =
	 *                  "SELECT c FROM ClassName c where c.someProperty=:param")
	 * 
	 * @param clazz
	 * @param param
	 * @return
	 */
	public List<? extends IEntity> getByCriteria(Class<? extends IEntity> clazz, Object param);

	/**
	 * The entityBean must define a Namedquery
	 * '"<clazz.simpleName>:getByCriterias' with one to several parameters. The
	 * map params hold the parameter's name/value ex : @NamedQuery(name =
	 * "ClassName:getByCriterias", query =
	 * "SELECT c FROM ClassName c where c.someProperty =:name1 and c.someOtherProp=:name2"
	 * )
	 * 
	 * @param clazz
	 * @param params
	 * @return
	 */
	public List<? extends IEntity> getByCriterias(Class<? extends IEntity> clazz, Map<String, Object> params);
	
	
	public List<? extends IEntity> getByQuery(String queryStr, Map<String, Object> params);

}
