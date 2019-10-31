package com.assignment.CSVApplication.services;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.assignment.CSVApplication.configurations.CSVFileUsersDetails;
import com.assignment.CSVApplication.dao.UserRepository;
import com.assignment.CSVApplication.models.Role;
import com.assignment.CSVApplication.models.User;


@ExtendWith(SpringExtension.class)
public class UserServiceTest {

	@Autowired
	private UserService userService;
	
	@MockBean
	private UserRepository userRepository;
	
	@MockBean
	private CSVFileUsersDetails csvFileUsersDetails;
	
	private volatile Map<User, Set<Role>> userRoleMap = new ConcurrentHashMap<User, Set<Role>>();
	
	@TestConfiguration
	static class UsersServiceTestContextConfiguration {

		@Bean
		public UserService userService() {
			return new UserService();
		}
	}
	
	@Test
	public void testFindAllUsers() throws Exception {
		
		userService.setUserRoleMap(userRoleMap);
		Set<User> dummyUsers = new HashSet<User>();
		Mockito.when(userRepository.findAll()).thenReturn(dummyUsers);
		
		Set<User> users = userService.findAllUsers();
		assertTrue(users.isEmpty());
	}

	@Test
	public void testFindByEmail() throws Exception {
		
		User dummyUser = new User();
		dummyUser.setEmail("abc@email.com");
		dummyUser.setName("Shashank");
		dummyUser.setId(2L);
		Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(dummyUser);
		User user = userService.findByEmail("abc@email.com");
		assertSame(dummyUser.getEmail(), user.getEmail());
		assertSame(dummyUser.getName(), user.getName());
		assertSame(dummyUser.getId(), user.getId());
	}
	
	@Test
	public void testDeleteUser() throws Exception {
		
		User user = new User();
		user.setEmail("abc@email.com");
		user.setName("Prasoon");
		user.setId(2L);
		Set<Role> roles = new HashSet<Role>();
		Role role = new Role();
		role.setRoleId(1l);
		role.setRoleName("Admin");
		roles.add(role);
		userRoleMap.put(user, roles);
		userService.setUserRoleMap(userRoleMap);

		User dummyUser = new User();
		dummyUser.setEmail("abc@email.com");
		dummyUser.setName("Shashank");
		dummyUser.setId(2L);
		Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(dummyUser);
		
		String response = userService.deleteUser(user);
		assertSame("User Deleted Successfully", response);
	}
	
	public void testDeleteAllUsers() throws Exception {
		
		User user = new User();
		user.setEmail("abc@email.com");
		user.setName("Shashank");
		user.setId(2L);
		Set<Role> roles = new HashSet<Role>();
		Role role = new Role();
		role.setRoleId(1l);
		role.setRoleName("Admin");
		roles.add(role);
		userRoleMap.put(user, roles);
		userService.setUserRoleMap(userRoleMap);
		Mockito.doNothing().when(userRepository).deleteAll();
		
		String response = userService.deleteAllUsers();
		assertSame("Users Deleted Successfully", response);
	}
}

