package com.giogio.services.utilities.UserEntityUpdateUtilities_implementations;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.giogio.DTO.UserDTO;
import com.giogio.entities.UserEntity;
import com.giogio.services.utilities.UserEntityUpdateUtilities;

@Primary
@Qualifier("userEntityUpdateUtilitiesImpl_V1")
@Component
public class UserEntityUpdateUtilitiesImpl_V1 implements UserEntityUpdateUtilities{

	@Override
	public void updateNameFromUserDTOIfPresent(UserDTO userDTO, UserEntity userEntity) {
		if(userDTO.getNameDTO()!=null) {
			userEntity.setName(userDTO.getNameDTO());
			}else{
				System.out.println("no changes have been made");
		}
		
	}

}
