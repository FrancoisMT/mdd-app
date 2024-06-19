package mdd_api.mdd_api.mapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import mdd_api.mdd_api.dto.CommentResponseDto;
import mdd_api.mdd_api.dto.PostDto;
import mdd_api.mdd_api.dto.PostResponseDto;
import mdd_api.mdd_api.dto.UserDto;
import mdd_api.mdd_api.entities.Post;
import mdd_api.mdd_api.entities.Topic;
import mdd_api.mdd_api.entities.User;

public class PostMapper {

	public static PostDto toDto(Post post) {
		
        if (post == null) {
            return null;
        }

        PostDto postDto = new PostDto();
        postDto.setId(post.getId());
        postDto.setTitle(post.getTitle());
        postDto.setDescription(post.getDescription());
        postDto.setDate(post.getDate());
        postDto.setUserId(post.getUser().getId());
        postDto.setTopicId(post.getTopic().getId());

        return postDto;
    }

    public static Post toEntity(PostDto postDto, User currentUser, Topic topic) {
    	
        if (postDto == null || currentUser == null || topic == null) {
             return null;
        }

        Post post = new Post();
        post.setId(postDto.getId());
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setUser(currentUser);
        post.setTopic(topic);
        
        return post;
    }
    
    public static List<PostDto> toDtoList(List<Post> posts) {
        return posts.stream().map(PostMapper::toDto).collect(Collectors.toList());
    }
    
    public static PostResponseDto toResponseDto(Post post, Topic topic, UserDto user, List<CommentResponseDto> comments) {
        PostResponseDto postResponseDto = new PostResponseDto();
        postResponseDto.setId(post.getId());
        postResponseDto.setTitle(post.getTitle());
        postResponseDto.setDescription(post.getDescription());
        postResponseDto.setDate(post.getDate());
        postResponseDto.setTopic(topic);
        postResponseDto.setUser(user);
        postResponseDto.setComments(comments != null ? comments : Collections.emptyList());
        
        return postResponseDto;
    }

}
