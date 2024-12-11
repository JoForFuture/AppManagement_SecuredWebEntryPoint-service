package com.giogio.DTO;

import com.giogio.services.UserDTO_FI;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
public class UserDTO implements UserDTO_FI<UserDTO>{
	
	private String nameDTO;
	private String surnameDTO;
	private Integer ageDTO;
	private String emailDTO;
	
	@Override
	public UserDTO myStream() {
		// TODO Auto-generated method stub
		return this;
	}
}
