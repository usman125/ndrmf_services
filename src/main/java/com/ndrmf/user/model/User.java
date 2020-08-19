package com.ndrmf.user.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.security.core.userdetails.UserDetails;

import com.ndrmf.config.audit.Auditable;
import com.ndrmf.setting.model.Department;
import com.ndrmf.setting.model.Designation;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User extends Auditable<String> implements UserDetails{
    private static final long serialVersionUID = 1L;

    private UUID id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
	private boolean enabled;
	
	private Organisation org;
	private List<Role> roles;
	
	private Department department;
	private Designation designation;
	
	private boolean isSAP;
	private boolean availableAsJv;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(columnDefinition = "uuid", updatable = false)
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
    
    @NotBlank
    @Size(max = 40)
    @Column(unique = true)
    public String getUsername() {
        return username;
    }

    public void setUsername(String userName) {
        this.username = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Size(max = 50)
    @Column(name = "first_name")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    @Transient
    public String getFullName() {
    	if(this.lastName == null)
    		return this.firstName;
    	else
    		return this.firstName + " " + this.lastName;
    }
    
    @Size(max = 50)
    @Column(name = "last_name")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    
    @NotBlank
    @Size(max = 100)
    @Column(unique = true)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Transient
	@Override
	public boolean isAccountNonExpired() {
		return false;
	}

	@Transient
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	

	@Transient
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	

	@Override
	public boolean isEnabled() {
		return enabled;
	}
	
	public void setEnabled(boolean enabled){
		this.enabled = enabled;
	}
	
	@Transient
	@Override
	public List<Role> getAuthorities() {
		return this.roles;
	}
	
	@ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	public List<Role> getRoles() {
		return this.roles;
	}
	
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	
	public void addRole(Role role){
		if(this.roles == null) {
			this.roles = new ArrayList<>();
		}
		this.roles.add(role);
	}

	@ManyToOne
	@JoinColumn(name="org_id", nullable = false)
	public Organisation getOrg() {
		return org;
	}

	public void setOrg(Organisation org) {
		this.org = org;
	}
	
	@ManyToOne
	@JoinColumn(name="department_id", nullable = true)
	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	@ManyToOne
	@JoinColumn(name="designation_id", nullable = true)
	public Designation getDesignation() {
		return designation;
	}

	public void setDesignation(Designation designation) {
		this.designation = designation;
	}

	@Column(columnDefinition = "boolean default false")
	public boolean isSAP() {
		return isSAP;
	}

	public void setSAP(boolean isSAP) {
		this.isSAP = isSAP;
	}

	public boolean isAvailableAsJv() {
		return availableAsJv;
	}

	public void setAvailableAsJv(boolean availableAsJv) {
		this.availableAsJv = availableAsJv;
	} 
}
