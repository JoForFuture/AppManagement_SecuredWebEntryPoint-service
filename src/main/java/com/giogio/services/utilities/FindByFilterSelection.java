package com.giogio.services.utilities;

import java.util.Optional;

import com.giogio.entities.UserEntity;

public interface FindByFilterSelection {
	
	public Optional<UserEntity> getOptionalUserEntity(Object filter,SearchFilterType searchFilterType);

}
