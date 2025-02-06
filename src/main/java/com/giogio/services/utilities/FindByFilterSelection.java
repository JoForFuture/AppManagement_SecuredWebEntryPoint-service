package com.giogio.services.utilities;

import java.util.List;
import java.util.Optional;

import com.giogio.entities.UserEntity;

public interface FindByFilterSelection {
	
	public Optional<UserEntity> getOptionalUserEntity(Object filter,SearchFilterTypeEnum searchFilterType);

	List<UserEntity> getUserEntityList(Object filter, SearchFilterTypeEnum searchFilterType);
	

}
