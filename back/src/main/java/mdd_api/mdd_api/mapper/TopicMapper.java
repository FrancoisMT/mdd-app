package mdd_api.mdd_api.mapper;

import java.util.List;
import java.util.stream.Collectors;

import mdd_api.mdd_api.dto.TopicDto;
import mdd_api.mdd_api.entities.Topic;

public class TopicMapper {
	
	public static TopicDto toDto(Topic topic) {
		
        if (topic == null) {
            return null;
        }

        TopicDto topicDto = new TopicDto();
        topicDto.setId(topic.getId());
        topicDto.setTitle(topic.getTitle());
        topicDto.setDescription(topic.getDescription());

        return topicDto;
    }

    public static Topic toEntity(TopicDto topicDto) {
    	
        if (topicDto == null) {
            return null;
        }

        Topic topic = new Topic();
        topic.setId(topicDto.getId());
        topic.setTitle(topicDto.getTitle());
        topic.setDescription(topicDto.getDescription());

        return topic;
    }
    
    public static List<TopicDto> toDtoList(List<Topic> topics) {
        return topics.stream().map(TopicMapper::toDto).collect(Collectors.toList());
    }

    public static List<Topic> toEntityList(List<TopicDto> topicDtos) {
        return topicDtos.stream().map(TopicMapper::toEntity).collect(Collectors.toList());
    }
}


