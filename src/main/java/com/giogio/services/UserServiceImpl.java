package com.giogio.services;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Predicate;

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
import com.giogio.services.utilities.SearchFilterTypeEnum;


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
	public Long addUserIfNotPresent( UserDTO userDTO) throws IllegalArgumentException, RuntimeException{
		
		if(userDTO==null||userDTO.getEmailDTO()==null||userDTO.getEmailDTO().isBlank()) {
			throw new IllegalArgumentException("at least one argument is null");
		}
		 return userRepository
					.findUserEntityByEmail(userDTO.getEmailDTO())
					.map((user)->{
						String message="user with name: \" "+user.getName()+" \" already exist";
						notificationSender.notifyMessage(message);
						throwMyRuntimeException(message);
						return -1l;
						})
					.orElseGet(()->{
						UserEntity savedUser=userRepository.save(fromUserDTOToUserEntity.doMapping(userDTO));
						notificationSender.notifyMessage("user with name: \" "+ savedUser +" \" saved");
						return savedUser.getId();
					});		 
	}
	
	private void throwMyRuntimeException(String message) {
		throw new RuntimeException(message);
	}
	

	//--------------------------READ--------------------------
	
	
	@Override
	public List<UserDTO> getUsersByDTOFields(UserDTO userDTO) throws NoSuchElementException {
		
		
		
		Predicate<UserDTO> lambdaFilter_IfNameNotNull = u ->	userDTO.getNameDTO() == null ||
														u.getNameDTO().equals(userDTO.getNameDTO());
			
		Predicate<UserDTO> lambdaFilter_IfAgeNotNullOrNotZero=u-> userDTO.getAgeDTO()==null ||
																 userDTO.getAgeDTO()==0	||
																 u.getAgeDTO().equals(userDTO.getAgeDTO());

		if(userDTO.getIdDTO()!=null&&userDTO.getIdDTO()!=0) {
			return 
					List.of( this.getUserById(userDTO.getIdDTO()) );	
		}else if(userDTO.getEmailDTO()!=null&&!userDTO.getEmailDTO().isBlank()) {
			return 
					List.of( this.getUserByEmail(userDTO.getEmailDTO()) );
		}else if(userDTO.getSurnameDTO()!=null&&!userDTO.getSurnameDTO().isBlank()) { 
			return 
					this.getUsersBySurname(userDTO.getSurnameDTO())
									.stream()
									.filter(lambdaFilter_IfNameNotNull)
									.filter(lambdaFilter_IfAgeNotNullOrNotZero)
									.toList();
		}else if(userDTO.getNameDTO()!=null&&!userDTO.getNameDTO().isBlank()) {
			return
					this.getUsersByName(userDTO.getNameDTO())
								.stream()
								.filter(lambdaFilter_IfAgeNotNullOrNotZero)
								.toList();
		}else if(userDTO.getAgeDTO()!=null && userDTO.getAgeDTO()!=0) {
			return 
					this.getUsersByAge(userDTO.getAgeDTO());
		}else if(userDTO.getNameSurnameDTO()!=null&&!userDTO.getNameSurnameDTO().isBlank()) {
			return 
					this.getUsersByNameAndSurname(userDTO.getNameSurnameDTO());
		}else {
			throw new NoSuchElementException("not found");
		}
		
//		userDTO.getUserByNameSurname(foundUsers, userDTO, this);
		
	
	}
	
	


	@Override
	public UserDTO getUserById(Long idDTO) throws NoSuchElementException{
		return 
				this.fromUserEntityToUserDTO.doMapping(userRepository.findById(idDTO)
																		 .orElseThrow());
	}
	
	@Override
	public UserDTO getUserByEmail(String emailDTO) throws NoSuchElementException{
		return 
				this.fromUserEntityToUserDTO.doMapping(userRepository.findUserEntityByEmail(emailDTO)
																		.orElseThrow());
	}
	
	@Override
	public List<UserDTO> getUsersByName(String nameDTO) throws NoSuchElementException{
		return 
				userRepository.findUserEntitiesByName(nameDTO)
								.orElseThrow()
								.stream()
								.map(userEntity->fromUserEntityToUserDTO.doMapping(userEntity))
								.toList();

	}
	
	@Override
	public List<UserDTO> getUsersBySurname(String surnameDTO) throws NoSuchElementException {
		return 
				userRepository.findUserEntitiesBySurname(surnameDTO)
								.orElseThrow()
								.stream()
								.map(userEntity->fromUserEntityToUserDTO.doMapping(userEntity))
								.toList();

	}
	
	
	@Override
	public List<UserDTO> getUsersByAge(Integer ageDTO) throws NoSuchElementException{
		return 
				userRepository.findUserEntitiesByAge(ageDTO)
								.orElseThrow()
								.stream()
								.map(userEntity->fromUserEntityToUserDTO.doMapping(userEntity))
								.toList();
		
	}
	
	@Override
	public List<UserDTO> getUsersByNameAndSurname(String nameAndSurnameDTO) throws NoSuchElementException{
		String[] nameSurnameArray=nameAndSurnameDTO.split("_");
		
		return 
				userRepository.findUserEntitiesByNameAndSurnameIgnoreCase(nameSurnameArray[0],nameSurnameArray[1])
								.orElseThrow()
								.stream()
								.map(userEntity->fromUserEntityToUserDTO.doMapping(userEntity))
								.toList();

	}


	

	@Deprecated
	@Override
	public List<UserDTO> getUsersByFilter(Object filter,SearchFilterTypeEnum searchFilterType) throws NoSuchElementException, IllegalArgumentException
	{	
		if(filter==null||searchFilterType==null) {
			throw new IllegalArgumentException("at least one argument is null");

		}
	
		
		return findByFilterSelection
				.getUserEntityList(filter, searchFilterType)
				.stream()
				.map(user->{
					notificationSender.notifyMessage("user "+user+" "
							+ "found");
					return fromUserEntityToUserDTO.doMapping(user);
				}).toList();
				
		
						
//				.orElseThrow(
//						() -> {
//							notificationSender.notifyMessage("User not found");
//							return new NoSuchElementException("User not found");
//			    });
		
	}
	
	
	@Deprecated
	@Override
	public UserDTO getSingleUserByFilter(Object filter,SearchFilterTypeEnum searchFilterType) throws NoSuchElementException, IllegalArgumentException
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
	public Long updateUserRecordByDto(UserDTO userDTO , Object searchFilterValue,SearchFilterTypeEnum searchFilterTypeEnum) throws IllegalArgumentException, NoSuchElementException {
		
		return findByFilterSelection
					.getOptionalUserEntity(searchFilterValue,searchFilterTypeEnum)
					.map(userEntity->userEntity
											.setNameFromNotNullAndNotBlank(userDTO)
											.setSurnameFromNotNullAndNotBlank(userDTO)
											.setAgeFromNotNullAndNotZero(userDTO)
											.setEmailFromNotNullAndNotBlank(userDTO))
					.map(userRepository::save)
					.map(userEntity->{
									notificationSender.notifyMessage("user : "+userEntity+" update successfully");
									return userEntity.getId(); })
					.orElseThrow(()->{
						notificationSender.notifyMessage("user : "+String.valueOf(searchFilterValue) +" update failed");
						throw new RuntimeException("user : "+String.valueOf(searchFilterValue) +" update failed");
					});
//					.orElseGet(()->{
//						return -1l;
//
//					 });
	}
					


	
	//--------------------------DELETE--------------------------

	@Transactional
	@Override
	public Long deleteUserById(Long id) throws IllegalArgumentException, NoSuchElementException, RuntimeException{
		if(	userRepository.existsById(id)) {
			userRepository.deleteById(id);
			notificationSender.notifyMessage("user deleted");
			return id;
			}else {
				notificationSender.notifyMessage("user delete failed. User not found");
				throw new RuntimeException("user delete failed. User not found");
			}
		}


	
		

		
	


		
	
}
