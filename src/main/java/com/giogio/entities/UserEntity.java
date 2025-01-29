package com.giogio.entities;


import com.giogio.DTO.UserDTO;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Builder
public class UserEntity {
	
	@Id
	@GeneratedValue (strategy=GenerationType.IDENTITY) 
	private Long id;
	@NotNull
	@NotEmpty
	private String name;
	@NotNull
	@NotEmpty
	private String surname;
//	@Min(10)
	private Integer age;
	@NotNull
	@NotEmpty
	private String email;
	

	@Override
	 public boolean equals(Object object) {
		
		if(object.getClass()==String.class) {
			if(isSameEmail((String)object)) {
				return true;
				}
			}
		
		if(object.getClass()==UserEntity.class) {
			if( this== object) {
				return true;
				}
			if(isSameEmail(((UserEntity)object).getEmail())) {
				return true;
				}
			}
		return false;
		}
	
	
	private boolean isSameEmail(String object) {
		if(
				this.getEmail().equalsIgnoreCase(object)
				) {
			return true;
		}else {
			return false;
		}
	}
	public UserEntity setSurnameFromNotNullAndNotBlank( UserDTO userDTO) {
		
		if(userDTO.getSurnameDTO()!=null && !(userDTO.getSurnameDTO().isBlank())) {
			
			this.surname=userDTO.getSurnameDTO();
			System.out.println("surname: updated");

	
		}else {
			System.out.println("surname: value not modified");
		}
		return this;

	}
	

	public UserEntity setNameFromNotNullAndNotBlank (UserDTO userDTO) {
				
		if(userDTO.getNameDTO()!=null && !(userDTO.getNameDTO().isBlank())) {
			
			this.name=userDTO.getNameDTO();
			System.out.println("name: updated");

	
		}else {
			System.out.println("name: value not modified");
		}
		return this;

	}
	
	public UserEntity setAgeFromNotNullAndNotZero( UserDTO userDTO) {
		
		if(userDTO.getAgeDTO()!=null && (userDTO.getAgeDTO()!=0)) {
			
			this.age=userDTO.getAgeDTO();
			System.out.println("age: updated");

	
		}else {
			System.out.println("age: value not modified");
		}
		return this;
	}
	
	public UserEntity setEmailFromNotNullAndNotBlank (UserDTO userDTO) {
		
		if(userDTO.getEmailDTO()!=null && !(userDTO.getEmailDTO().isBlank())) {
			
			this.email=userDTO.getEmailDTO();
			System.out.println("email: updated");

	
		}else {
			System.out.println("email: value not modified");
		}
		return this;

	}

	
	}
