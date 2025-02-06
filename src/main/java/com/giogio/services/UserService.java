package com.giogio.services;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;

import com.giogio.DTO.UserDTO;
import com.giogio.entities.UserEntity;
import com.giogio.services.utilities.SearchFilterTypeEnum;

public interface UserService {
		
	Long addUserIfNotPresent(UserDTO userDTO)throws IllegalArgumentException;
	
	List<UserDTO> getUsersByDTOFields(UserDTO userDTO) throws NoSuchElementException;
	
	UserDTO getSingleUserByFilter(Object filter,SearchFilterTypeEnum searchFilterType) throws NoSuchElementException;
	
	List<UserDTO> getUsersByFilter(Object filter, SearchFilterTypeEnum searchFilterType) throws NoSuchElementException, IllegalArgumentException;

	List<UserDTO> getAllUsers() throws NotFoundException;
	
	Long updateUserRecordByDto(UserDTO userDTO ,Object filter,SearchFilterTypeEnum searchFilterType) throws IllegalArgumentException, NoSuchElementException;

	Long deleteUserById(Long id) throws IllegalArgumentException, NoSuchElementException;

	UserDTO getUserById(Long idDTO) throws NoSuchElementException;

	UserDTO getUserByEmail(String emailDTO) throws NoSuchElementException;

	List<UserDTO> getUsersByName(String nameDTO) throws NoSuchElementException;

	List<UserDTO> getUsersBySurname(String surnameDTO) throws NoSuchElementException;

	List<UserDTO> getUsersByAge(Integer ageDTO) throws NoSuchElementException;

	List<UserDTO> getUsersByNameAndSurname(String nameAndSurnameDTO) throws NoSuchElementException;

	


	



}
