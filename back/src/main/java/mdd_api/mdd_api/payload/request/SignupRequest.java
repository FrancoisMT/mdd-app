package mdd_api.mdd_api.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignupRequest {

	  @NotBlank
	  @Size(max = 50)
	  @Email
	  private String email;

	  @NotBlank
	  @Size(max = 30)
	  private String username;

	  @NotBlank
	  @Size(min = 8, max = 40)
	  private String password;
	
}
