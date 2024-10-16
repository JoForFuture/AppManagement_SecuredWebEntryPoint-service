package com.giogio.repositories;

import java.util.Optional;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.giogio.entities.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {
	
	Optional<UserEntity> findUserEntityById(Long id);

//	Optional<UserEntity> findByNameIgnoreCase(String nameDTO);
	
	Optional<UserEntity> findUserEntityByNameAndSurnameIgnoreCase(String nameDTO, String surname);
	
	Optional<UserEntity> findUserEntityByEmail(String email);


	
//	void updateUserEntityByUserEntity(UserEntity userEntity);

}
