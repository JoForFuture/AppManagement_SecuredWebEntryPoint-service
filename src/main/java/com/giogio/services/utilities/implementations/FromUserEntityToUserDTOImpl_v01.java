package com.giogio.services.utilities.implementations;

import org.springframework.stereotype.Component;

import com.giogio.DTO.UserDTO;
import com.giogio.entities.UserEntity;
import com.giogio.services.utilities.FromUserEntityToUserDTO;

//no test
@Component
public class FromUserEntityToUserDTOImpl_v01 implements FromUserEntityToUserDTO{

	@Override
	public UserDTO doMapping(UserEntity userEntity) throws IllegalArgumentException {
		if(userEntity==null) {
			throw new IllegalArgumentException("userEntity is null");
		}
		
		return UserDTO.builder()
					.idDTO(userEntity.getId()==null?0 : userEntity.getId())
					.surnameDTO(userEntity.getSurname()==null ? "" : userEntity.getSurname())
					.nameDTO(userEntity.getName()==null ? "" : userEntity.getName())
					.ageDTO(userEntity.getAge()==null ? 0 : userEntity.getAge())
					.emailDTO(userEntity.getEmail()==null ? "" : userEntity.getEmail())
					.build();
	}

}
