package mdd_api.mdd_api.entities;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;



@SuppressWarnings("serial")
@Data
@Entity
@Table(name="users")
public class User implements UserDetails {

	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

	@Email(message = "Please enter a valid email")
    @NotNull(message = "Email cannot be NULL")
    @Column(nullable = false)
    private String mail;
	
	@NotNull(message = "username cannot be NULL")
    @Column(nullable = false)
	@Size(max=50)
    private String name;    

	@NotNull(message = "password cannot be NULL")
	@Column(nullable = false)
	private String password;
	
	@OneToMany(mappedBy = "user")
    private List<Subscription> subscriptions;
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of();
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		 return this.mail;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
	
	
}
