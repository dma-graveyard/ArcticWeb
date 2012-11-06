package dk.dma.arcticweb.ejb.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;

import org.apache.commons.codec.digest.DigestUtils;

@Entity
@NamedQueries( {
	@NamedQuery(name = "User:getByUsername", query = "SELECT u FROM User u WHERE u.username=:username")
})
public class User extends AbstractEntity {
	
	private static final String PASSWORD_SALT = "fa26frADu8";
	
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
	
	@Column(nullable = false, length = 256)
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
	
	@Transient
	public void setPassword(String password) {
		setPassowordHash(hashPassword(password));
	}
	
	@Transient
	public static String hashPassword(String password) {
		return DigestUtils.sha256Hex(DigestUtils.sha256Hex(password) + PASSWORD_SALT);
	}
	
}
