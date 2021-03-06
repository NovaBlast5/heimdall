package com.demo.authserver.service;

import com.demo.authserver.entity.*;
import com.demo.authserver.model.CustomException;
import com.demo.authserver.repository.AppUserRepository;
import com.demo.authserver.repository.RoleResourcePrivilegeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {

	private final AppUserRepository appUserRepository;
	private final RoleResourcePrivilegeRepository roleResourcePrivilegeRepository;

	@Autowired
	public UserDetailsServiceImpl(AppUserRepository appUserRepository, RoleResourcePrivilegeRepository roleResourcePrivilegeRepository) {
		this.appUserRepository = appUserRepository;
		this.roleResourcePrivilegeRepository = roleResourcePrivilegeRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws CustomException {
		final Optional<AppUser> userOptional = appUserRepository.findByUsername(username);

		if (!userOptional.isPresent()) {
			throw new CustomException("User with username: " + username + " doesn't exist", HttpStatus.NOT_FOUND);
		}

		final AppUser user = userOptional.get();
		return new org.springframework.security.core.userdetails.User(username, user.getPassword(),
				getAuthorities(user.getRoles()));
	}

	private List<? extends GrantedAuthority> getAuthorities(
			Set<Role> roles) {
		return getGrantedAuthorities(getPrivileges(roles));
	}

	private List<String> getPrivileges(Set<Role> roles) {

		List<String> privilegeNames = new ArrayList<>();
		List<Privilege> privileges = new ArrayList<>();
		for (Role role : roles) {
			for(Resource resource : role.getRoleResources()) {
				RoleResourcePrivilege roleResourcePrivilege = roleResourcePrivilegeRepository.findByRoleAndResource(role, resource).get();
				privileges.addAll(roleResourcePrivilege.getPrivileges());
			}
		}
		for (Privilege item : privileges) {
			privilegeNames.add(item.getName());
		}
		return privilegeNames;
	}

	private static List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
		List<GrantedAuthority> authorities = new ArrayList<>();
		for (String privilege : privileges) {
			authorities.add(new SimpleGrantedAuthority(privilege));
		}
		return authorities;
	}

}
