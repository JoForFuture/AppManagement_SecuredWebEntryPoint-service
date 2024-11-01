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
import com.giogio.services.utilities.NotificationSender;

@Validated
@Service
public class UserServiceImpl implements UserService {

	
	private UserRepository userRepository;
	private FromUserDTOToUserEntity fromUserDTOToUserEntity;
	private NotificationSender notificationSender;
	private FindByFilterSelection findByFilterSelection;
	
	
	//-------------------------- Constructor--------------------------
	public UserServiceImpl(
			UserRepository userRepository,
			FromUserDTOToUserEntity fromUserDTOToUserEntity,
			NotificationSender notificationSender,
			FindByFilterSelection findByFilterSelection
			) {
		this.userRepository=userRepository;
		this.fromUserDTOToUserEntity=fromUserDTOToUserEntity;
		this.notificationSender=notificationSender;
		this.findByFilterSelection=findByFilterSelection;
		
	}
	
	
	//--------------------------CREATE--------------------------
/**
 * @param UserDTO userDTO
 * @param String email
 * @return void
 * @implNote Check if user is present by email parameter,
 * 			 if already exist it will send notification with this information and nothing else,
 * 			 otherwise,
 * 			 will create new user with information issued and send confirm notification.
 */
	@Transactional
	@Override
	public void addUserIfNotPresent( UserDTO userDTO, String email) throws IllegalArgumentException{
		
		 userRepository
		 .findUserEntityByEmail(email)
		 .ifPresentOrElse(
				 (user)->{
					 notificationSender.notifyMessage("user with name: \" "+user.getName()+" \" already exist");
					 }
				 ,
				 ()->{
					 UserEntity savedUser=userRepository.save(fromUserDTOToUserEntity.doMapping(userDTO,email));
					 notificationSender.notifyMessage("user with name: \" "+ savedUser +" \" saved");
					 }
				 );
		 
	}
	
	//--------------------------READ--------------------------
	
	@Transactional
	@Override
	public UserEntity getUserByEmailOrId(Object filter) throws NoSuchElementException { 
	
		return 
				findByFilterSelection
						.getOptionalUserEntity(filter)
						.map(
								(user)->{
									notificationSender.notifyMessage("user found");
									return user;
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
	public List<UserEntity> getAllUsers() throws NotFoundException {
		
		return 
				userRepository
						.findAll()
						.stream()
						.map(
								(list)->{
									notificationSender.notifyMessage("all users getted");
									return list;
								}
						)
						.toList();
			
		
	}

//	/**
//	 * @param Long id
//	 * @return UserEntity userEntity
//	 * @implNote Find user by userId,
//	 * 			 than,
//	 * 			 if exist, will return it and send notification,
//	 * 			 otherwise,
//	 * 			 send notification and throws  NoSuchElementException
//	 * @exception NoSuchElementException if no element was found
//	 */
//	@Transactional
//	@Override
//	public UserEntity getUserById(Long id) throws NoSuchElementException { 
//		
//		return userRepository.findUserEntityById(id)
//			    .map(u -> {
//			        notificationSender.notifyMessage("User found");
//			        return u;
//			    })
//			    .orElseThrow(() -> {
//			        notificationSender.notifyMessage("User not found");
//			        return new NoSuchElementException("User with id " + id + " not found");
//			    });
//	
//	}
//	
//	/**
//	 * @param String email
//	 * @return UserEntity userEntity
//	 * @implNote Find user by user email,
//	 * 			 than,
//	 * 			 if exist, will return it and send notification,
//	 * 			 otherwise,
//	 * 			 send notification and throws  NoSuchElementException
//	 * @exception NoSuchElementException if no element was found
//	 */
//	@Transactional
//	@Override
//	public UserEntity getUserByEmail(String email) throws NoSuchElementException { 
//		
//		return 
//				userRepository
//				.findUserEntityByEmail(email)
//				.map(
//						(u)->{
//							notificationSender.notifyMessage("user found");
//							return u;
//							}
//						)
//				.orElseThrow(() -> {
//			        notificationSender.notifyMessage("User not found");
//			        return new NoSuchElementException("User with email " + email + " not found");
//			    });				
//		}
//	
	


	//--------------------------UPDATE --------------------------
	
	@Transactional
	@Override
	public void updateUserRecordByDto(UserDTO userDTO , Object searchFilterValue) throws IllegalArgumentException, NoSuchElementException {
	
//		FIGOOOOO
		userDTO
			.nameNotNullAndNotEmpty("StingaProva", (s)->{
				return userDTO;
			});	
			
		findByFilterSelection
			.getOptionalUserEntity(searchFilterValue)
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
