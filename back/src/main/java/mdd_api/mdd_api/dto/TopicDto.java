package mdd_api.mdd_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TopicDto {
	
	private Long id;
	
	@NotBlank
	@Size(max = 200)
	private String title;
	
	@NotBlank
	@Size(max = 2500)
	private String description;
	
}
