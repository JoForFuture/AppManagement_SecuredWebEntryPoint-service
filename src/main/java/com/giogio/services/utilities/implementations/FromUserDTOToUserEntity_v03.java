package com.giogio.services.utilities.implementations;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.giogio.DTO.UserDTO;
import com.giogio.entities.UserEntity;
import com.giogio.services.utilities.FromUserDTOToUserEntity;

@Primary
@Qualifier("fromUserDTOToUserEntity_v03")
@Component
public class FromUserDTOToUserEntity_v03 implements FromUserDTOToUserEntity{


	@Override
	public UserEntity doMapping(UserDTO userDTO)throws IllegalArgumentException{
		if(userDTO==null) {
			throw new IllegalArgumentException("at least one argument is null");
		}
		return UserEntity.builder()
						.id(userDTO.getIdDTO())
						.name(userDTO.getNameDTO()==null ? "" : userDTO.getNameDTO())
						.surname(userDTO.getSurnameDTO()==null ? "" : userDTO.getSurnameDTO())
						.age(userDTO.getAgeDTO()==null ? 0 : userDTO.getAgeDTO())
						.email(userDTO.getEmailDTO()==null ? "":userDTO.getEmailDTO())
						.build();
	
				} 
}
