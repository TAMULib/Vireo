package org.tdl.vireo.controller;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.tdl.vireo.model.User;

import edu.tamu.weaver.auth.model.Credentials;
import edu.tamu.weaver.response.ApiStatus;

@SuppressWarnings("unchecked")
public class UserControllerTest extends AbstractControllerTest {

	@Test
	public void testCredentials() {
		response = userController.credentials(TEST_CREDENTIALS);
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		Credentials credentials = (Credentials) response.getPayload().get("Credentials");
		assertEquals(" The credential last name is incorrect ", TEST_USER_LAST_NAME , credentials.getLastName());
		assertEquals(" The credential first name is incorrect ", TEST_USER_FIRST_NAME , credentials.getFirstName());
		assertEquals(" The credential email is incorrect ", TEST_USER_EMAIL , credentials.getEmail());
	}

	@Test
	public void testAllUsers() {
		response = userController.allUsers();
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		List<User> userList = (List<User>) response.getPayload().get("ArrayList<User>");
		assertEquals("There are no users present in the list " , mockUsers.size() , userList.size());
	}

	@Test
	public void testAllAssignableUsers()  throws Exception {
		response = userController.allAssignableUsers();
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		List<User> userList = ((List<User>) response.getPayload().get("ArrayList<User>"));
		assertEquals("There are no assignable users present in the list " , 3 , userList.size());
	}

	@Test
	public void testUpdateUser() {
		TEST_USER.setRole(TEST_USER_ROLE);
		response = userController.update(TEST_USER);
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		User updatedUser = (User) response.getPayload().get("User");
		assertEquals(" The user does not have the updated role ", TEST_USER.getRole() , updatedUser.getRole());
	}

	@Test
	public void testGetUserSettings() {
		Map<String, String> userSettings = new HashMap<String, String> ();
		userSettings.put("setting1", "setting1");
		userSettings.put("setting2", "setting2");
		TEST_USER.setSettings(userSettings);
		response = userController.getSettings(TEST_USER);
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		Map<String, String> responseSettings = (Map<String, String>) response.getPayload().get("HashMap");
		assertEquals(userSettings, responseSettings);
	}

	@Test
	public void testUpdateUserSetting() {
		Map<String, String> userSettings = new HashMap<String, String> ();
		userSettings.put("setting1", "setting1");
		userSettings.put("setting2", "setting2");
		response = userController.updateSetting(TEST_USER, userSettings);
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
	}
}
