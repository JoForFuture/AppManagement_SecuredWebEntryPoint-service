package com.giogio.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.giogio.entities.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {
	
	Optional<UserEntity> findUserEntityById(Long id);
	
	Optional<List<UserEntity>> findUserEntityByNameAndSurnameIgnoreCase(String nameDTO, String surname);
	
	Optional<UserEntity> findUserEntityByEmail(String email);

	Optional<List<UserEntity>> findUserEntityByAge(Integer filter);

	Optional<List<UserEntity>> findUserEntityByName(String filter);

	Optional<List<UserEntity>> findUserEntityBySurname(String filter);


	
//	void updateUserEntityByUserEntity(UserEntity userEntity);

}
