package org.tdl.vireo.controller;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import org.tdl.vireo.model.CustomActionDefinition;

import edu.tamu.weaver.response.ApiStatus;

@ActiveProfiles("test")
public class CustomActionSettingsControllerTest extends AbstractControllerTest {

	@SuppressWarnings("unchecked")
	@Test
	public void testGetCustomActions() {
		response = customActionSettingsController.getCustomActions();
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		List<CustomActionDefinition> customActionDefinitionList = (List<CustomActionDefinition>) response.getPayload().get("ArrayList<CustomActionDefinition>");
		assertEquals("There are no custom actio definition in the list ", 2 , customActionDefinitionList.size() );
	}

	@Test
	public void testCreateCustomAction() {
		response = customActionSettingsController.createCustomAction(TEST_CUSTOM_ACTION_DEF1);
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		CustomActionDefinition createdCustomActionDefinition = (CustomActionDefinition) response.getPayload().get("CustomActionDefinition");
		assertEquals(" The created custom action definition does not have the correct label ", TEST_CUSTOM_ACTION_DEF1.getLabel() , createdCustomActionDefinition.getLabel());
		assertEquals(" The created custom action definition does not have the correct student visibility ", TEST_CUSTOM_ACTION_DEF1.isStudentVisible() , createdCustomActionDefinition.isStudentVisible());
	}

	@Test
	public void testUpdateCustomAction() {
		TEST_CUSTOM_ACTION_DEF1.setLabel("Updated Label for Custom Action Definition 1");
		response = customActionSettingsController.updateCustomAction(TEST_CUSTOM_ACTION_DEF1);
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		CustomActionDefinition updatedCustomActionDefinition = (CustomActionDefinition) response.getPayload().get("CustomActionDefinition");
		assertEquals("The custom action label was not updated ", TEST_CUSTOM_ACTION_DEF1.getLabel() , updatedCustomActionDefinition.getLabel());
	}

	@Test
	public void testRemoveCustomAction() {
		//TODO
		response = customActionSettingsController.removeCustomAction(TEST_CUSTOM_ACTION_DEF2);
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
	}

	@Test
	public void testReorderCustomActions() {
		//TODO
		response = customActionSettingsController.reorderCustomActions(TEST_CUSTOM_ACTION_DEF2.getId(), TEST_CUSTOM_ACTION_DEF1.getId());
	}
}
