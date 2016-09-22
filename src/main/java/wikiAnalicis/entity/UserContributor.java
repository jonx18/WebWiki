package wikiAnalicis.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.google.gson.Gson;

/**
 * Representa al autor de una revision.
 * El mismo puede ser registrado, anonimo, o no existir actualmente.
 * @see Revision
 * @author Jonathan Martin
 *
 */
@Entity
@Table(name="usercontributor")
public class UserContributor {
	//Contador de anonimos
	public static Long anonimusID=new Long(-1);
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="id")
    //es un id para desambiguar en caso de repeticion del id real.
	private Long id;

    //representa el id del usuario en la wiki o un identificador negativo de ser anonimo
    @Column(nullable = false,name="realid")
    private Long realId=anonimusID;
    @Column(nullable = false,unique=true,name="username")
	private String username;
    @Column(name="ip")
	private String ip;
    @Column(name="deleted")
	private Boolean deleted=false;
	public UserContributor() {
		// TODO Auto-generated constructor stub
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * Retorna el nombre de usuario si el mismo no es anonimo, en caso contrario retornara null o vacio.
	 * @return String
	 */
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * Retorna la ip si el mismo es anonimo, en caso contrario retornara null o vacio.
	 * @return String
	 */
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	/**
	 * Retorna el id que representa al usuario en la wiki
	 * @return Long
	 */
	public Long getRealId() {
		return realId;
	}
	public void setRealId(Long realId) {
		this.realId = realId;
	}
	@Override
	public String toString() {
		Gson gson = new Gson();
		return gson.toJson(this, getClass());
	}
	//Tuve que sobre escribir equals y hashcode
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
	       if (!(obj instanceof UserContributor)){
	            return false;
	            }
	        if (obj == this){
	            return true;
	        }
	        UserContributor rhs = (UserContributor) obj;
	        return new EqualsBuilder().
	            // if deriving: appendSuper(super.equals(obj)).
	        		append(id, rhs.getId()).
	        		append(username, rhs.getUsername()).
	        		append(ip, rhs.getIp()).
	            isEquals();
		
	}
	@Override
	public int hashCode() {
        return new HashCodeBuilder(17, 31). // two randomly chosen prime numbers
                // if deriving: appendSuper(super.hashCode()).
                append(id).
                append(username).
                append(ip).
                toHashCode();

	}
	public void setAnonimus() {
		this.id=UserContributor.anonimusID;
		UserContributor.anonimusID = anonimusID-1;
	}
	/**
	 * retorna si el usuario fue eliminado.
	 * @return Boolean
	 */
	public Boolean getDeleted() {
		return deleted;
	}
	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}
}
