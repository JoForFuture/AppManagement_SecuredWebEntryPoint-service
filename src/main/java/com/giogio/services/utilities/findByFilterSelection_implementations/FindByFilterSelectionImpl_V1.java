package com.giogio.services.utilities.findByFilterSelection_implementations;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.giogio.entities.UserEntity;
import com.giogio.repositories.UserRepository;
import com.giogio.services.utilities.FindByFilterSelection;

@Primary
@Qualifier("FindByFilterSelectionImpl_V1")
@Component
public class FindByFilterSelectionImpl_V1 implements FindByFilterSelection {
	
	private final UserRepository userRepository;
	
	
	public FindByFilterSelectionImpl_V1(UserRepository userRepository) {
		this.userRepository=userRepository;
	}

	@Override
	public Optional<UserEntity> getOptionalUserEntity(Object filter){
		Optional<UserEntity> user;
		if(filter.getClass().equals(Long.class)) {
			user=userRepository.findById((Long)filter);
			return user;
		}else if(filter.getClass().equals(String.class)){	
			user=userRepository.findUserEntityByEmail((String)filter);
			return user;
		}else {
			throw new RuntimeException("wrong filter type");
		}
	}

}
