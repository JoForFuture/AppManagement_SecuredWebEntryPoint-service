package com.giogio.services.utilities;

import java.util.List;
import java.util.NoSuchElementException;

import com.giogio.DTO.UserDTO;
import com.giogio.services.UserService;

public interface UserDTOUtilities {
	
	
	public default boolean filterWithNotNullFields(UserDTO userDTO) {
		
		
		
		return true;
	}
	
	
	public default UserDTO getUserById(List<UserDTO> foundUsers, UserDTO userDTO, UserService userService) {
		try {foundUsers.addAll(userService.getUsersByFilter(userDTO.getIdDTO(), SearchFilterTypeEnum.ID));}
		catch(NoSuchElementException nsee) { }
		catch(IllegalArgumentException nsee) {};
		return userDTO;

	}
	
	public default UserDTO getUserByEmail(List<UserDTO> foundUsers, UserDTO userDTO, UserService userService) {
		try {foundUsers.addAll(userService.getUsersByFilter(userDTO.getEmailDTO(), SearchFilterTypeEnum.EMAIL));}
		catch(NoSuchElementException nsee) { }
		catch(IllegalArgumentException nsee) {};
		return userDTO;

	}
	
	public default UserDTO getUserByName(List<UserDTO> foundUsers, UserDTO userDTO, UserService userService) {
		try {foundUsers.addAll(userService.getUsersByFilter(userDTO.getNameDTO(), SearchFilterTypeEnum.NAME));}
		catch(NoSuchElementException nsee) { }
		catch(IllegalArgumentException nsee) {};
		return userDTO;

	}
	
	public default UserDTO getUserBySurname(List<UserDTO> foundUsers, UserDTO userDTO, UserService userService) {
		try {foundUsers.addAll(userService.getUsersByFilter(userDTO.getSurnameDTO(), SearchFilterTypeEnum.SURNAME));}
		catch(NoSuchElementException nsee) { }
		catch(IllegalArgumentException nsee) {};
		return userDTO;

	}

		
	public default UserDTO getUserByAge(List<UserDTO> foundUsers, UserDTO userDTO, UserService userService) {
		try {foundUsers.addAll(userService.getUsersByFilter(userDTO.getAgeDTO(), SearchFilterTypeEnum.AGE));}
		catch(NoSuchElementException nsee) { }
		catch(IllegalArgumentException nsee) {};
		return userDTO;
		
	}
	
	public default UserDTO getUserByNameSurname(List<UserDTO> foundUsers, UserDTO userDTO, UserService userService) {
		try {foundUsers.addAll(userService.getUsersByFilter(userDTO.getNameSurnameDTO(), SearchFilterTypeEnum.NAME_SURNAME));}
		catch(NoSuchElementException nsee) { }
		catch(IllegalArgumentException nsee) {};
		return userDTO;
		
	}

	
//	public default 
	
	

//	

//	


	
	

}
