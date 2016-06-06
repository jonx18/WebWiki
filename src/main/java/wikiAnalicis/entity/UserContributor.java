package wikiAnalicis.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.GenericGenerator;

import com.google.gson.Gson;

@Entity
public class UserContributor implements Identificable{
	/**
	 * 
	 */
	public static Long anonimusID=new Long(-1);
	private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
    @Column(nullable = false)
    private Long realId=anonimusID;
    @Column(nullable = false,unique=true)
	private String username;
	private String ip;
	public UserContributor() {
		// TODO Auto-generated constructor stub
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
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
}
