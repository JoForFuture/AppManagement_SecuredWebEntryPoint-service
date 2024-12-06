package com.giogio.services.utilities.implementations;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.giogio.DTO.UserDTO;
import com.giogio.entities.UserEntity;
import com.giogio.services.utilities.FromUserDTOToUserEntity;

@Primary
@Qualifier("fromUserDTOToUserEntity_v02")
@Component
public class FromUserDTOToUserEntity_v02 implements FromUserDTOToUserEntity{


	@Override
	public UserEntity doMapping(UserDTO userDTO, String email)throws NullPointerException{
		if(userDTO==null) {
			throw new NullPointerException("userDTO passed as argument is null");
		}
		return UserEntity.builder()
						.name(userDTO.getNameDTO()==null ? "" : userDTO.getNameDTO())
						.surname(userDTO.getSurnameDTO()==null ? "" : userDTO.getSurnameDTO())
						.age(userDTO.getAgeDTO()==null ? 0 : userDTO.getAgeDTO())
						.email(email==null ? "":email)
						.build();
	
				} 
}
