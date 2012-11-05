package dk.dma.arcticweb.ejb.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class User extends AbstractEntity {
	
	private static final long serialVersionUID = 1L;
	
	private String username;
	private String passowordHash;
	private String email;
	
	public User() {
		super();
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(unique = true, nullable = false)
	@Override
	public Integer getId() {
		return id;
	}
	
	@Column(unique = true, nullable = false, length = 32)
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	@Column(nullable = false, length = 128)
	public String getPassowordHash() {
		return passowordHash;
	}

	public void setPassowordHash(String passowordHash) {
		this.passowordHash = passowordHash;
	}

	@Column(nullable = false, length = 128)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
}
