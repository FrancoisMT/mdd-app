package mdd_api.mdd_api.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PostDto {

	private Long id;
	
	@NotNull(message = "Title cannot be NULL")
	@Size(max = 200)
	private String title;
	
	@NotNull(message = "Description cannot be NULL")
	@Size(max = 2500)
	private String description;
	
	private LocalDateTime date;

	@NotNull(message = "User id cannot be NULL")
	private Long userId;
	
	@NotNull(message = "Topic id cannot be NULL")
	private Long topicId;
	
}
