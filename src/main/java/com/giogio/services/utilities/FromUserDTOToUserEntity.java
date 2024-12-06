package com.giogio.services.utilities;

import com.giogio.DTO.UserDTO;
import com.giogio.entities.UserEntity;

public interface FromUserDTOToUserEntity {

	public UserEntity doMapping(UserDTO userDTO, String email) ;
	
}
