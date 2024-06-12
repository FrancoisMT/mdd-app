package mdd_api.mdd_api.entities;

import java.util.List;

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



@Data
@Entity
@Table(name="users")
public class User {

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
    private String username;    

	@NotNull(message = "password cannot be NULL")
	@Column(nullable = false)
	private String password;
	
	@OneToMany(mappedBy = "user")
    private List<Subscription> subscriptions;
	
	
}
