package com.giogio.services.utilities;

import com.giogio.DTO.UserDTO;
import com.giogio.entities.UserEntity;

public interface FromUserEntityToUserDTO {
	
	public UserDTO doMapping(UserEntity userEntity) ;


}
