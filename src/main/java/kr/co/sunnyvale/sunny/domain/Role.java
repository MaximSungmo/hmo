package kr.co.sunnyvale.sunny.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table( name = "ROLE" )
//@Cache(usage= CacheConcurrencyStrategy.READ_WRITE)
public class Role implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = -87712028024561955L;
	
	public static final int ID_SUPER_ADMIN = 0;
	public static final int ID_ADMIN = 1;
	public static final int ID_USER = 2;
	public static final int ID_INACTIVE_ADMIN = 100;
	public static final int ID_INACTIVE_USER = 101;
	
	public static final String ROLE_SUPER_ADMIN = "SUPER_ADMIN";
	public static final String ROLE_ADMIN = "ADMIN";
	public static final String ROLE_USER = "USER";
	public static final String ROLE_INACTIVE_ADMIN = "INACTIVE_ADMIN";
	public static final String ROLE_INACTIVE_USER= "INACTIVE_USER";
	
	
	@Id
//	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column(unique = true)
	private String name;

	public Role(){
		
	}
	public Role(int id, String name, String description){
		this.id = id;
		this.name = name;
		this.description = description;
	}
	public Role(int id){
		this.setId(id);
	}
	public Role(String id){
		this.setId(Integer.valueOf(id));
	}
	
	@Column
	private String description;


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj){
			return true;
		}if (obj == null){
			
			return false;
		}if (getClass() != obj.getClass()){
			return false;
		}
		Role other = (Role) obj;
		
		if (id != other.id){
			return false;
		}
		return true;
	}


}
