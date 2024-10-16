package com.giogio.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
public class UserDTO {
	
	private String nameDTO;
	private String surnameDTO;
}
