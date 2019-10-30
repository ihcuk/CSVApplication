package com.assignment.CSVApplication.configurations;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Component;

import com.assignment.CSVApplication.models.UserDTO;

@Component
public class CSVFileUsersDetails {

	private volatile int noOfLinesFailed = 0;
	private volatile List<UserDTO> validDTOList ;
	private volatile List<UserDTO> invalidDTOList;

	public int getNoOfLinesFailed() {
		return noOfLinesFailed;
	}

	public void setNoOfLinesFailed(int noOfLinesFailed) {
		this.noOfLinesFailed = noOfLinesFailed;
	}

	public List<UserDTO> getValidDTOList() {
		return validDTOList;
	}

	public void setValidDTOList(List<UserDTO> validDTOList) {
		this.validDTOList = validDTOList;
	}

	public List<UserDTO> getInvalidDTOList() {
		return invalidDTOList;
	}

	public void setInvalidDTOList(List<UserDTO> invalidDTOList) {
		this.invalidDTOList = invalidDTOList;
	}

	public synchronized void  parseUserDTOList(List<UserDTO> usersDTOList) {
		validDTOList = new ArrayList<UserDTO>();
		invalidDTOList = new ArrayList<UserDTO>();
		usersDTOList.forEach(userDTO -> {
			String invalidErrorMessage = "";
			if(!isEmailValid(userDTO.getEmail())) {
				invalidErrorMessage = CSVApplicationConstants.INVALID_EMAIL_PREFIX;
			}
			String invalidRoles = getInvalidRoles(userDTO,userDTO.getRoles());
			if(!invalidRoles.isEmpty()) {
				invalidErrorMessage = (invalidErrorMessage.isEmpty() ? invalidErrorMessage : invalidErrorMessage + "#") + CSVApplicationConstants.INVALID_ROLE_PREFIX + invalidRoles;
			}
			if(!invalidErrorMessage.isEmpty()) userDTO.setInvalid(true);
			userDTO.setErrorMessage(invalidErrorMessage);
			if(userDTO.isInvalid())
				invalidDTOList.add(userDTO);
			else
				validDTOList.add(userDTO);
		});
		noOfLinesFailed = invalidDTOList.size();
	}
	
	public String getInvalidRoles(UserDTO userDto, String roles) {
		Set<String> rolesSet = userDto.getRolesSet(roles);
		StringBuilder invalidRoles = new StringBuilder();
		rolesSet.forEach(role -> {
			if(CSVApplicationConstants.VALID_ROLES.indexOf(role) < 0) {
				invalidRoles.append(role);
			}
		});
		return invalidRoles.toString();
	}
	
	public boolean isEmailValid(String email) {
		return email.matches(CSVApplicationConstants.EMAIL_REGEX);
	}
	
}
