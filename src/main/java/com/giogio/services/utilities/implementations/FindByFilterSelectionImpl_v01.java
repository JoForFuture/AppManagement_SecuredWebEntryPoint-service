package com.giogio.services.utilities.implementations;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.giogio.entities.UserEntity;
import com.giogio.repositories.UserRepository;
import com.giogio.services.utilities.FindByFilterSelection;
import com.giogio.services.utilities.SearchFilterTypeEnum;

@Primary
@Qualifier("FindByFilterSelectionImpl_v01")
@Component
public class FindByFilterSelectionImpl_v01 implements FindByFilterSelection {
	
	private final UserRepository userRepository;
	
	
	public FindByFilterSelectionImpl_v01(UserRepository userRepository) {
		this.userRepository=userRepository;
	}

	@Override
	public Optional<UserEntity> getOptionalUserEntity(Object filter,SearchFilterTypeEnum searchFilterType){
		Optional<UserEntity> user;
		
		
		switch(searchFilterType) {
		
		case ID:
			user=userRepository.findById((Long)filter);
			return user;
			
		case EMAIL:
			user=userRepository.findUserEntityByEmail((String)filter);
			return user;
	
		default:
			throw new RuntimeException("wrong filter type");
		}
				
			
	}
	
	@Override
	public List<UserEntity> getUserEntityList(Object filter,SearchFilterTypeEnum searchFilterType){
		
		final List<UserEntity> userList=new LinkedList<>();
		
		switch(searchFilterType) {
		
		case ID:
			 userRepository.findById((Long)filter).ifPresent(user->userList.add(user));
			 break;
		case AGE:
			userRepository.findUserEntitiesByAge((Integer)filter).ifPresent(list->userList.addAll(list));
			break;
		case NAME:
			userRepository.findUserEntitiesByName((String)filter).ifPresent(list->userList.addAll(list));	
			break;
		case SURNAME:
			userRepository.findUserEntitiesBySurname((String)filter).ifPresent(list->userList.addAll(list));
			break;
		case EMAIL:
			userRepository.findUserEntityByEmail((String)filter).ifPresent(user->userList.add(user));
			break;
		case NAME_SURNAME:	
			String nameSurname=(String)filter;
			String[] nameSurnameArray=nameSurname.split("_");
			userRepository.findUserEntitiesByNameAndSurnameIgnoreCase(nameSurnameArray[0],nameSurnameArray[1]).ifPresent(list->userList.addAll(list));
			break;
		default:
			throw new RuntimeException("wrong filter type");
		}
		
		return userList;
			
	}
}
