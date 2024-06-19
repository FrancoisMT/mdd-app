package mdd_api.mdd_api.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentResponseDto {

	@Size(max=2500)
	private String content;
	
	private UserDto user;
	
	private LocalDateTime date;
	
}
