package com.login.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.login.model.User;
import com.login.web.dto.UserRegistrationDTO;

public interface UserService extends UserDetailsService {
	
	User save(UserRegistrationDTO registrationDTO);
}
