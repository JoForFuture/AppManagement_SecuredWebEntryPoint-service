package com.giogio.services;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;

import com.giogio.DTO.UserDTO;
import com.giogio.entities.UserEntity;

public interface UserService {
		
	void addUserIfNotPresent(UserDTO userDTO, String email)throws IllegalArgumentException;
	
	UserEntity getUserByEmailOrId(Object filter) throws NoSuchElementException;

//	UserEntity getUserById(Long id) throws NoSuchElementException;
//	UserEntity getUserByEmail(String email) throws NoSuchElementException;

	List<UserEntity> getAllUsers() throws NotFoundException;

	void updateUserRecordByDto(UserDTO userDTO ,Object filter) throws IllegalArgumentException, NoSuchElementException;
	
	boolean deleteUserById(Long id) throws IllegalArgumentException, NoSuchElementException;
	



}
