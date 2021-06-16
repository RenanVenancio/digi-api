package com.techzone.digi.security;

import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.techzone.digi.entity.Company;

public class UserSS implements UserDetails {

	private static final long serialVersionUID = 1L;
	private UUID id;
	private String email;
	private String password;
	private Company company;
	private Collection<? extends GrantedAuthority> authorities;

	public UserSS() {

	}

	public UserSS(UUID id, String email, String password, Boolean admin, Company company) {
		super();
		this.id = id;
		this.email = email;
		this.password = password;
		this.company = company;

		SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(
				(admin ? "ROLE_ADMIN" : "ROLE_CLIENT"));
		this.authorities = Arrays.asList(simpleGrantedAuthority);
	}

	public UUID getId() {
		return id;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return password;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isAdmin() {
		return getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
	}

}
