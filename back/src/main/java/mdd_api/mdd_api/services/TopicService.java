package mdd_api.mdd_api.services;

import java.util.List;

import org.springframework.stereotype.Service;

import mdd_api.mdd_api.entities.Topic;
import mdd_api.mdd_api.repositories.TopicRepository;

@Service
public class TopicService {

	public TopicRepository repository;
	
	public TopicService(TopicRepository repository) {
		this.repository = repository;
	}
	
	
	public List<Topic> getAll() {
		
		List<Topic> list = this.repository.findAll();
		
		return list;
	}
	
	
	
}
