package com.giogio.services.utilities.implementations;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.giogio.DTO.UserDTO;
import com.giogio.entities.UserEntity;
import com.giogio.services.utilities.FromUserDTOToUserEntity;

@Primary
@Qualifier("fromUserDTOToUserEntityMyImpl_V1")
@Component
public class FromUserDTOToUserEntityMyImpl_V1 implements FromUserDTOToUserEntity{

	@Override
	public UserEntity doMapping(UserDTO userDTO, String email){
		return UserEntity.builder()
						.name(userDTO.getNameDTO())
						.surname(userDTO.getSurnameDTO())
						.age(userDTO.getAgeDTO())
						.email(email)
						.build();
	
				} 
}
