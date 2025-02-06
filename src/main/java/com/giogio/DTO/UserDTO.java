package com.giogio.DTO;

import java.util.Set;

import com.giogio.services.utilities.UserDTOUtilities;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
public class UserDTO implements UserDTOUtilities, Comparable<UserDTO>{
	
	private Long idDTO;
	
	 @Min(value = 0, message = "L'età deve essere almeno 0.")
	 @Max(value = 100, message = "L'età non può superare 100.")
	private Integer ageDTO;
	
    @Email(message = "L'email non è valida.")
	private String emailDTO;

    @Size(min = 1, max = 50, message = "La stringa deve avere una lunghezza tra 1 e 50 caratteri.")
	private String nameDTO, surnameDTO, nameSurnameDTO;
    
    public void userDTOvalidation(UserDTO request, Validator validator) throws RuntimeException{
		Set<ConstraintViolation<UserDTO>> violations = validator.validate(request);

        if (!violations.isEmpty()) {
            StringBuilder errors = new StringBuilder();
            for (ConstraintViolation<UserDTO> violation : violations) {
                errors.append(violation.getMessage()).append("; ");
            }
            System.err.println( "Errore di validazione: " + errors.toString());
            throw new RuntimeException("Errore di validazione: " + errors.toString());
        }
	}
    
	@Override
	public int compareTo(UserDTO o) {
		return surnameDTO.compareTo(o.getSurnameDTO());
	}
	
	public UserDTO and() {
		return this;
		
	}
}
