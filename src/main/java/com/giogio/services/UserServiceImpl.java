package com.giogio.services;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.giogio.DTO.UserDTO;
import com.giogio.entities.UserEntity;
import com.giogio.repositories.UserRepository;
import com.giogio.services.utilities.FindByFilterSelection;
import com.giogio.services.utilities.FromUserDTOToUserEntity;
import com.giogio.services.utilities.FromUserEntityToUserDTO;
import com.giogio.services.utilities.NotificationSender;
import com.giogio.services.utilities.SearchFilterType;

@Validated
@Service
public class UserServiceImpl implements UserService {

	
	private UserRepository userRepository;
	private FromUserDTOToUserEntity fromUserDTOToUserEntity;
	private FromUserEntityToUserDTO fromUserEntityToUserDTO;
	private NotificationSender notificationSender;
	private FindByFilterSelection findByFilterSelection;
	
	
	//-------------------------- Constructor--------------------------
	public UserServiceImpl(
			UserRepository userRepository,
			FromUserDTOToUserEntity fromUserDTOToUserEntity,
			FromUserEntityToUserDTO fromUserEntityToUserDTO,
			NotificationSender notificationSender,
			FindByFilterSelection findByFilterSelection
			) {
		this.userRepository=userRepository;
		this.fromUserDTOToUserEntity=fromUserDTOToUserEntity;
		this.fromUserEntityToUserDTO=fromUserEntityToUserDTO;
		this.notificationSender=notificationSender;
		this.findByFilterSelection=findByFilterSelection;
		
		
	}
	
	
	//--------------------------CREATE--------------------------
/**
 * @param UserDTO userDTO
 * @param String email
 * @return Long
 * @implNote Check if user is present by email parameter,
 * 			 if already exist it will send notification with this information and nothing else,
 * 			 otherwise,
 * 			 will create new user with information issued and send confirm notification.
 */
	@Transactional
	@Override
	public Long addUserIfNotPresent( UserDTO userDTO) throws IllegalArgumentException{
		
		if(userDTO==null||userDTO.getEmailDTO()==null||userDTO.getEmailDTO().isBlank()) {
			throw new IllegalArgumentException("at least one argument is null");
		}
		return userRepository
					.findUserEntityByEmail(userDTO.getEmailDTO())
					.map((user)->{
						notificationSender.notifyMessage("user with name: \" "+user.getName()+" \" already exist");
						return -1l;
						})
					.orElseGet(()->{
						UserEntity savedUser=userRepository.save(fromUserDTOToUserEntity.doMapping(userDTO));
						notificationSender.notifyMessage("user with name: \" "+ savedUser +" \" saved");
						return savedUser.getId();
					});		 
	}
	

	//--------------------------READ--------------------------
	
	@Transactional
	@Override
	public UserDTO getUserByFilter(Object filter,SearchFilterType searchFilterType) throws NoSuchElementException, IllegalArgumentException
	{	
		if(filter==null||searchFilterType==null) {
			throw new IllegalArgumentException("at least one argument is null");

		}
	
		return 
				findByFilterSelection
						.getOptionalUserEntity(filter,searchFilterType)
						.map(
								(user)->{
									notificationSender.notifyMessage("user found");
									return fromUserEntityToUserDTO.doMapping(user);
									}
								)
						.orElseThrow(
								() -> {
									notificationSender.notifyMessage("User not found");
									return new NoSuchElementException("User not found");
					    });
	}
	
	
	
	
	
	@Transactional 
	@Override
	public List<UserDTO> getAllUsers() throws NotFoundException {
		
		return 
				userRepository
						.findAll()
						.stream()
						.map(
								(user)->{
									notificationSender.notifyMessage("user "+user.getEmail()+" retrived");
									return fromUserEntityToUserDTO.doMapping(user);
								}
						)
						.toList();
			
		
	}

	//--------------------------UPDATE --------------------------
	
	
	@Transactional
	@Override
	public Long updateUserRecordByDto(UserDTO userDTO , Object searchFilterValue,SearchFilterType searchFilterType) throws IllegalArgumentException, NoSuchElementException {
		
		return findByFilterSelection
					.getOptionalUserEntity(searchFilterValue,searchFilterType)
					.map(userEntity->userEntity
											.setNameFromNotNullAndNotBlank(userDTO)
											.setSurnameFromNotNullAndNotBlank(userDTO)
											.setAgeFromNotNullAndNotZero(userDTO)
											.setEmailFromNotNullAndNotBlank(userDTO))
					.map(userRepository::save)
					.map(userEntity->{
									notificationSender.notifyMessage("user : "+userEntity+" update successfully");
									return userEntity.getId(); })
					.orElseGet(()->{
						notificationSender.notifyMessage("user : "+String.valueOf(searchFilterValue) +" update failed");
						return -1l;

					 });
	}
					


	
	//--------------------------DELETE--------------------------

	@Transactional
	@Override
	public boolean deleteUserById(Long id) throws IllegalArgumentException, NoSuchElementException{
		if(	userRepository.existsById(id)) {
			userRepository.deleteById(id);
			notificationSender.notifyMessage("user deleted");
			return true;
			}else {
				notificationSender.notifyMessage("user delete failed. User not found");
				throw new NoSuchElementException();
			}
		}
		

		
	


		
	
}
