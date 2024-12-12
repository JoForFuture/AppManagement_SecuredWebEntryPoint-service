package com.giogio.services;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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
	public UserDTO getUserByFilter(Object filter,SearchFilterType searchFilterType) throws NoSuchElementException {
	
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
	public void updateUserRecordByDto(UserDTO userDTO , Object searchFilterValue,SearchFilterType searchFilterType) throws IllegalArgumentException, NoSuchElementException {
	
//		FIGOOOOO
//		userDTO
//			.nameNotNullAndNotEmpty("StingaProva", (s)->{
//				return userDTO;
//			});	
			
		findByFilterSelection
			.getOptionalUserEntity(searchFilterValue,searchFilterType)
			.map(
					user->{//void 
						Optional.of(userDTO.getNameDTO())
									.filter(u->userDTO.getNameDTO() != null && !userDTO.getNameDTO().isBlank())
									.ifPresentOrElse(
												value->user.setName(value),
											()->{
												System.out.println("value not modified");
											});
						
						Optional.of(userDTO.getSurnameDTO())
						.filter(u->userDTO.getSurnameDTO() != null && !userDTO.getSurnameDTO().isBlank())
						.ifPresentOrElse(
									value->user.setSurname(value),
								()->{
									System.out.println("value not modified");
								});
						
						Optional.of(userDTO.getAgeDTO())
						.filter(u->userDTO.getAgeDTO() != null)
						.ifPresentOrElse(
									value->user.setAge(value),
								()->{
									System.out.println("value not modified");
								});
					
						
						return user;
						}
					) .ifPresentOrElse(
							 (user)->{
									userRepository.save(user);
									notificationSender.notifyMessage("user : "+user+" update successfully");
								 }
							 ,
							 ()->{
									notificationSender.notifyMessage("user : "+String.valueOf(searchFilterValue) +" update failed");

								 }
							 );
			
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
