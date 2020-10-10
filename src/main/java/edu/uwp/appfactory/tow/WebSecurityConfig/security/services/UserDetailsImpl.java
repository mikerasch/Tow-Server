package edu.uwp.appfactory.tow.WebSecurityConfig.security.services;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.uwp.appfactory.tow.WebSecurityConfig.models.ERole;
import edu.uwp.appfactory.tow.WebSecurityConfig.models.Role;
import edu.uwp.appfactory.tow.testingEntities.Users;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class UserDetailsImpl implements UserDetails {
	private static final long serialVersionUID = 1L;

	private final String id;

	private final String username;

	private final String email;

	@JsonIgnore
	private final String password;

	private String firstname;

	private String lastname;

	private final String role;

	public UserDetailsImpl(String id, String username, String email, String password, String firstname, String lastname,
						   String role) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.password = password;
		this.firstname = firstname;
		this.lastname = lastname;
		this.role = role;
	}

	public static UserDetailsImpl build(Users user) {
		return new UserDetailsImpl(
				user.getId(),
				user.getUsername(),
				user.getEmail(),
				user.getPassword(),
				user.getFirstname(),
				user.getLastname(),
				user.getRoles());
	}

	public String getRole() {
		return role;
	}

	public String getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		ERole eRole;

		if (getRole().equals("ROLE_USER"))
		{
			eRole = ERole.ROLE_USER;
		}
		else if (getRole().equals("ROLE_DISPATCHER"))
		{
			eRole = ERole.ROLE_DISPATCHER;
		}
		else
		{
			eRole = ERole.ROLE_ADMIN;
		}

		Role tempRole = new Role();

		tempRole.setName(eRole);

		Collection<Role> roles = new ArrayList<>();

		roles.add(tempRole);

		List<GrantedAuthority> authorities = roles.stream()
				.map(role -> new SimpleGrantedAuthority(role.getName().name()))
				.collect(Collectors.toList());


		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		UserDetailsImpl user = (UserDetailsImpl) o;
		return Objects.equals(id, user.id);
	}
	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
}
