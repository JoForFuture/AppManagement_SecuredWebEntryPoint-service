package com.giogio.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
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
	}
