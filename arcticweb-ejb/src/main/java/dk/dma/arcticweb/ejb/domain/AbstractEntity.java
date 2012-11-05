package dk.dma.arcticweb.ejb.domain;

/**
 * Base class for all entity beans
 */
public abstract class AbstractEntity implements IEntity {

	private static final long serialVersionUID = 1L;

	protected Integer id;

	public boolean isNew() {
		return getId() == null;
	}

	public boolean isPersisted() {
		return !isNew();
	}

}
