package com.assignment.CSVApplication.models;

import java.util.LinkedHashSet;
import java.util.Set;

import com.opencsv.bean.CsvBindByName;

public class UserDTO {

	@CsvBindByName(column = "EMAIL", required = true)
	private String email;
	
	@CsvBindByName(column = "NAME")
	private String name;
	
	@CsvBindByName(column = "ROLES")
	private String roles;
	
	//Optional 
	@CsvBindByName(column = "ERRORS", required=false)
	private String errorMessage;
	@CsvBindByName(column = "validity", required=false)
	boolean isInvalid = false;
	
	public UserDTO() {}
	public UserDTO(UserDTO userDTO) {
		this.email = userDTO.email;
		this.name = userDTO.name;
		this.roles = userDTO.roles;
		this.errorMessage = userDTO.errorMessage;
	}
	
	public UserDTO(String email, String name, String roles) {
		this(email, name, roles, null);
	}
	
	public UserDTO(String email, String name, String roles, String errorMessage) {
		this.email = email;
		this.name = name;
		this.roles = roles;
		this.errorMessage = errorMessage;
	}

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRoles() {
		return roles;
	}
	public void setRoles(String roles) {
		this.roles = roles;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	public boolean isInvalid() {
		return isInvalid;
	}

	public void setInvalid(boolean isInvalid) {
		this.isInvalid = isInvalid;
	}

	public Set<String> getRolesSet(String roles) {
		Set<String> roleSet = new LinkedHashSet<String>();
		if(roles != null ) {
			for(String role : roles.split("#"))roleSet.add(role);
		}
		return roleSet;
	}
	
	public User getUserFromUserDTO(UserDTO userDTO) {
		Set<Role> rolesSet = new LinkedHashSet<Role>();
		if(userDTO != null) {
			User user = new User();
			Set<String> rolesString = userDTO.getRolesSet(userDTO.getRoles());
			rolesString.forEach(roleString -> {
				Role role = new Role();
				role.setRoleName(roleString);;
				rolesSet.add(role);
			});
			
			user.setEmail(userDTO.getEmail());
			user.setName(userDTO.getName());
			user.setRoles(rolesSet);
			return user;
		} else {
			return null;
		}
			
	}

}
