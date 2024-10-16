package com.giogio.services.utilities.FromUserDTOToUserEntity_implementations;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.giogio.DTO.UserDTO;
import com.giogio.entities.UserEntity;
import com.giogio.services.utilities.FromUserDTOToUserEntity;

@Primary
@Qualifier("fromUserDTOToUserEntityMyImpl")
@Component
public class FromUserDTOToUserEntityMyImpl implements FromUserDTOToUserEntity{

	@Override
	public UserEntity doMapping(UserDTO userDTO, String email){
		return UserEntity.builder()
						.name(userDTO.getNameDTO())
						.surname(userDTO.getSurnameDTO())
						.email(email)
						.build();
	
				} 
}
