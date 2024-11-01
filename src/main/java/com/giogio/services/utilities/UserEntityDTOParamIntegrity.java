package com.giogio.services.utilities;

public interface UserEntityDTOParamIntegrity {
	
	static boolean nameNotNulAndNotEmpty(String name) {
	    return name != null && !name.isBlank();
	}
	

}
