package mdd_api.mdd_api.mapper;

import mdd_api.mdd_api.dto.UserDto;
import mdd_api.mdd_api.entities.User;

public class UserMapper {

	public static UserDto toDto(User user) {
		
		if (user == null) {
			return null;
		}
		
		UserDto userDto = new UserDto();
		userDto.setId(user.getId());
		userDto.setUsername(user.getName());
		
		return userDto;
	}
	
}
