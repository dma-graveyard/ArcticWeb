package dk.dma.arcticweb.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Ship extends Stakeholder {
	private static final long serialVersionUID = 1L;
	
	private Long mmsi;
	private ShipOwner owner;
	
	public Ship() {
	}
	
	@Column(nullable = true)
	public Long getMmsi() {
		return mmsi;
	}
	
	public void setMmsi(Long mmsi) {
		this.mmsi = mmsi;
	}
	
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(nullable = true)
	public ShipOwner getOwner() {
		return owner;
	}
	
	public void setOwner(ShipOwner owner) {
		this.owner = owner;
	}

}
