package com.giogio.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
public class UserDTO implements Comparable<UserDTO>{
	
	private String nameDTO;
	private String surnameDTO;
	private Integer ageDTO;
	private String emailDTO;
	
	@Override
	public int compareTo(UserDTO o) {
		return surnameDTO.compareTo(o.getSurnameDTO());
	}
}
