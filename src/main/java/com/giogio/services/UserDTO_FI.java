package com.giogio.services;

import java.util.function.Function;

@FunctionalInterface
public interface UserDTO_FI<T> {
	
	public T myStream();
	
	default T  nameNotNullAndNotEmpty(String name, Function<String,T> actionIfNotNull) {
	    if( name != null && !name.isBlank()) {
	    	return actionIfNotNull.apply(name);
	    }
	    System.err.println("no values was modified");
	    return myStream();
	}
	
}
