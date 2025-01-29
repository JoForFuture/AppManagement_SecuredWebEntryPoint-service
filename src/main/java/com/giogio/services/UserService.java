package com.giogio.services;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;

import com.giogio.DTO.UserDTO;
import com.giogio.entities.UserEntity;
import com.giogio.services.utilities.SearchFilterType;

public interface UserService {
		
	Long addUserIfNotPresent(UserDTO userDTO)throws IllegalArgumentException;
	
	List<UserDTO> getUsersByDTO(UserDTO userDTO) throws NoSuchElementException;
	
	UserDTO getSingleUserByFilter(Object filter,SearchFilterType searchFilterType) throws NoSuchElementException;
	
	List<UserDTO> getUsersByFilter(Object filter, SearchFilterType searchFilterType) throws NoSuchElementException, IllegalArgumentException;

	List<UserDTO> getAllUsers() throws NotFoundException;
	
	Long updateUserRecordByDto(UserDTO userDTO ,Object filter,SearchFilterType searchFilterType) throws IllegalArgumentException, NoSuchElementException;

	Long deleteUserById(Long id) throws IllegalArgumentException, NoSuchElementException;

	


	



}
