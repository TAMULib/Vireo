package org.tdl.vireo.controller;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import org.tdl.vireo.model.User;

import edu.tamu.weaver.response.ApiResponse;
import edu.tamu.weaver.response.ApiStatus;

@ActiveProfiles("test")
public class AuthControllerTest extends AbstractControllerTest {

    @Test
    @SuppressWarnings("unchecked")
    public void testRegisterEmail() {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("email", TEST_EMAIL);

        response = authController.registration(null, parameters);
        assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
        assertEquals(TEST_EMAIL, ((String) ((Map<String, String>) response.getPayload().get("HashMap")).get("email")));
    }

    @Test
    public void testRegister() throws Exception {
        String token = cryptoService.generateGenericToken(TEST_USER_EMAIL, EMAIL_VERIFICATION_TYPE);
        Map<String, String> data = new HashMap<String, String>();
        data.put("token", token);
        data.put("email", TEST_USER_EMAIL);
        data.put("firstName", TEST_USER_FIRST_NAME);
        data.put("lastName", TEST_USER_LAST_NAME);
        data.put("password", TEST_USER_PASSWORD);
        data.put("confirm", TEST_USER_CONFIRM);

        ApiResponse response = authController.registration(data, new HashMap<String, String>());

        User user = (User) response.getPayload().get("User");

        assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());

        assertEquals(TEST_USER_FIRST_NAME, user.getFirstName());
        assertEquals(TEST_USER_LAST_NAME, user.getLastName());
        assertEquals(TEST_USER_EMAIL, user.getEmail());
        assertEquals(TEST_USER_ROLE, user.getRole());
    }

    @Test
    public void testLogin() throws Exception {
        testRegister();
        Map<String, String> data = new HashMap<String, String>();
        data.put("email", TEST_USER_EMAIL);
        data.put("password", TEST_USER_PASSWORD);
        ApiResponse response = authController.login(data);
        assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
    }
}
