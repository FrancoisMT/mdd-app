package mdd_api.mdd_api.dto;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import mdd_api.mdd_api.entities.Topic;

@Data
public class PostResponseDto {

private Long id;
	
	@NotNull(message = "Title cannot be NULL")
	@Size(max = 200)
	private String title;
	
	@NotNull(message = "Description cannot be NULL")
	@Size(max = 2500)
	private String description;
	
	private LocalDateTime date;

	@NotNull
	private UserDto user;
	
	@NotNull
	private Topic topic;
	
	private List<CommentResponseDto> comments;
	
}
