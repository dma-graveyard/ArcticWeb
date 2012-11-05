package dk.dma.arcticweb.ejb.domain;

import java.io.Serializable;

/**
 * Interface for all entities
 */
public interface IEntity extends Serializable {

	Integer getId();

	boolean isNew();

	boolean isPersisted();

}
