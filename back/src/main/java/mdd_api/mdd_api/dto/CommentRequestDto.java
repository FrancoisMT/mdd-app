package mdd_api.mdd_api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentRequestDto {

	@Size(max=2500)
	@NotNull
	private String content;
	
}
