package com.giogio.services.utilities;

import com.giogio.DTO.UserDTO;
import com.giogio.entities.UserEntity;

public interface UserEntityUpdateUtilities {
	
	public void updateNameFromUserDTOIfPresent(UserDTO userDTO, UserEntity userEntity);


}
