package mdd_api.mdd_api.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import mdd_api.mdd_api.dto.TopicDto;
import mdd_api.mdd_api.entities.Topic;
import mdd_api.mdd_api.mapper.TopicMapper;
import mdd_api.mdd_api.services.TopicService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/topic")
public class TopicController {

	public TopicService service;
	
	public TopicController(TopicService service) {
		this.service= service;	
	}
	
	@GetMapping("/list")
	@Operation(summary = "Retrieve topic list")
	@ApiResponses(value = { 
	        @ApiResponse(responseCode = "200", description = "list successfully retrieved", 
	        		content = @Content(mediaType = "application/json")),
	        @ApiResponse(responseCode = "400", description = "Bad request", content = @Content), 
	        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
	})
	public ResponseEntity<List<TopicDto>> getAll() {

		List<Topic> list = service.getAll();
		List<TopicDto> dtoList = TopicMapper.toDtoList(list);

		return ResponseEntity.ok().body(dtoList);
		
	}
	
	
	
}
