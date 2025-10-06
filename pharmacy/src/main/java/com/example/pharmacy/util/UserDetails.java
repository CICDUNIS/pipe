package com.example.pharmacy.util;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserDetails {
	public String getUsuarioActual() {
	    if (SecurityContextHolder.getContext().getAuthentication() != null) {
		    return SecurityContextHolder.getContext().getAuthentication().getName();
	    }
	    return "anonymous";
    }
}

