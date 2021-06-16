package com.techzone.digi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.techzone.digi.entity.User;
import com.techzone.digi.repository.ClientRepository;
import com.techzone.digi.security.UserSS;

@Service
public class UserDetailServiceImplementation implements UserDetailsService{

	@Autowired
	private ClientRepository clientRepository;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = clientRepository.findByEmail(email);
		if(user == null) {
			throw new UsernameNotFoundException(email);
		}
		return new UserSS(user.getId(), user.getEmail(), user.getPassword(), user.getIsAdmin(), user.getCompany());
	}

}
