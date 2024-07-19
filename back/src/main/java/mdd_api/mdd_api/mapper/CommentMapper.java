package mdd_api.mdd_api.mapper;

import java.util.List;
import java.util.stream.Collectors;

import mdd_api.mdd_api.dto.CommentRequestDto;
import mdd_api.mdd_api.dto.CommentResponseDto;
import mdd_api.mdd_api.entities.Comment;
import mdd_api.mdd_api.entities.Post;
import mdd_api.mdd_api.entities.User;

public class CommentMapper {

	public static CommentResponseDto toDto(Comment comment) {
        if (comment == null) {
            return null;
        }
        
        CommentResponseDto dto = new CommentResponseDto();
        dto.setContent(comment.getContent());
        dto.setDate(comment.getDate());
        dto.setUser(UserMapper.toDto(comment.getUser()));
        
        return dto;
    }

    public static List<CommentResponseDto> toDtoList(List<Comment> comments) {
        return comments.stream()
                .map(CommentMapper::toDto)
                .collect(Collectors.toList());
    }
    
    
    public static Comment toEntity(CommentRequestDto commentDto, User user, Post post) {
    	
    	if (commentDto == null || user == null || post == null) {
            return null;
        }
    	
    	Comment comment = new Comment();
    	comment.setContent(commentDto.getContent());
    	comment.setUser(user);
    	comment.setPost(post);
    	
    	return comment;	
    }
	
}
