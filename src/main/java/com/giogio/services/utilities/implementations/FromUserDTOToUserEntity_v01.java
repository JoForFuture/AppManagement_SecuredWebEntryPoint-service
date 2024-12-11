package com.giogio.services.utilities.implementations;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.giogio.DTO.UserDTO;
import com.giogio.entities.UserEntity;
import com.giogio.services.utilities.FromUserDTOToUserEntity;

@Qualifier("fromUserDTOToUserEntity_v01")
@Component
public class FromUserDTOToUserEntity_v01 implements FromUserDTOToUserEntity{

	@Override
	public UserEntity doMapping(UserDTO userDTO)throws NullPointerException{
		if(userDTO==null) {
			throw new NullPointerException("userDTO passed as argument is null");
		}
		return UserEntity.builder()
						.name(userDTO.getNameDTO())
						.surname(userDTO.getSurnameDTO())
						.age(userDTO.getAgeDTO())
						.email(userDTO.getEmailDTO())
						.build();
	
				} 
	
}
