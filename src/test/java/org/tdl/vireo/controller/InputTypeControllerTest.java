package org.tdl.vireo.controller;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.tdl.vireo.model.InputType;

import edu.tamu.weaver.response.ApiStatus;

public class InputTypeControllerTest extends AbstractControllerTest {

	@Test
	public void testGetAllInputTypes() {
		response = inputTypeController.getAllInputTypes();
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		@SuppressWarnings("unchecked")
		List<InputType> inputTypeList = (List<InputType>) response.getPayload().get("ArrayList<InputType>");
		assertEquals("There are no input types in the list ", mockInputTypeList.size(), inputTypeList.size());
	}
}