package dk.dma.arcticweb.ejb.domain;

import java.io.Serializable;

/**
 * Interface for all entities
 */
public interface IEntity extends Serializable {

	Integer getId();
	
	void setId(Integer id);

	boolean isNew();

	boolean isPersisted();

}
