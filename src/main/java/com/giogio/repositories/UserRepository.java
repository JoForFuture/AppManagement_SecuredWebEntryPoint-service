package com.giogio.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.giogio.entities.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {
	
	
	Optional<List<UserEntity>> findUserEntitiesByNameAndSurnameIgnoreCase(String nameDTO, String surnameDTO);
	
	Optional<UserEntity> findUserEntityByEmail(String email);

	Optional<List<UserEntity>> findUserEntitiesByAge(Integer filter);

	Optional<List<UserEntity>> findUserEntitiesByName(String filter);

	Optional<List<UserEntity>> findUserEntitiesBySurname(String filter);


	
//	void updateUserEntityByUserEntity(UserEntity userEntity);

}
