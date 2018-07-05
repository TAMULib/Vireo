package org.tdl.vireo.controller;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.tdl.vireo.model.FieldProfile;

import edu.tamu.weaver.response.ApiStatus;

public class FieldProfileControllerTest extends AbstractControllerTest {

	@SuppressWarnings("unchecked")
	@Test
	public void testGetAllFieldProfiles() {
		response = fieldProfileController.getAllFieldProfiles();
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		List<FieldProfile> fieldProfileList = (List<FieldProfile>) response.getPayload().get("ArrayList<FieldProfile>");
		assertEquals(" The list size of field profile is incorrect ", mockFieldProfileList.size() , fieldProfileList.size() );
	}
}
