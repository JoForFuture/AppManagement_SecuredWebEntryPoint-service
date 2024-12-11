package com.giogio.services;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;

import com.giogio.DTO.UserDTO;
import com.giogio.entities.UserEntity;
import com.giogio.services.utilities.SearchFilterType;

public interface UserService {
		
	Long addUserIfNotPresent(UserDTO userDTO)throws IllegalArgumentException;
	
	UserDTO getUserByFilter(Object filter,SearchFilterType searchFilterType) throws NoSuchElementException;

	List<UserDTO> getAllUsers() throws NotFoundException;

	void updateUserRecordByDto(UserDTO userDTO ,Object filter,SearchFilterType searchFilterType) throws IllegalArgumentException, NoSuchElementException;
	
	boolean deleteUserById(Long id) throws IllegalArgumentException, NoSuchElementException;


	



}
