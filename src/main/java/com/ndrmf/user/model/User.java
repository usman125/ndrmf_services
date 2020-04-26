package com.ndrmf.user.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.security.core.userdetails.UserDetails;

import com.ndrmf.config.audit.Auditable;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User extends Auditable<String> implements UserDetails{

    private static final long serialVersionUID = 1L;

    private long id;
    private String username;
    private String password;
    private String firstName;
    private String familyName;
    private String email;
	private boolean enabled;
	
	private Organisation org;
	private List<Role> roles;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId() {
        return id;
    }

    public void setId(long id) {
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
    
    @Size(max = 50)
    @Column(name = "last_name")
    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
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
}
