package org.tdl.vireo.controller;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.test.context.ActiveProfiles;
import org.tdl.vireo.model.CustomActionDefinition;

import edu.tamu.weaver.response.ApiStatus;

@ActiveProfiles("test")
public class CustomActionSettingsControllerTest extends AbstractControllerTest {

	public 	CustomActionDefinition createCustomActionDefinition(String label, Boolean isStudentVisible) {
		return new CustomActionDefinition(label, isStudentVisible);
	}

	public 	CustomActionDefinition updateCustomActionDefinition(CustomActionDefinition modifiedCustomActionDefinition) {
		CustomActionDefinition cadObj = null;
		for(CustomActionDefinition customActionDefinition : mockCustomActionDefList) {
			if(modifiedCustomActionDefinition.getId().equals(customActionDefinition.getId())) {
				customActionDefinition.setLabel(modifiedCustomActionDefinition.getLabel());
				customActionDefinition.isStudentVisible(modifiedCustomActionDefinition.isStudentVisible());
				cadObj = customActionDefinition;
				break;
			}
		}
		return cadObj;
	}

	protected static CustomActionDefinition TEST_CUSTOM_ACTION_DEF1 = new CustomActionDefinition("Custom Action Label 1", false);
	protected static CustomActionDefinition TEST_CUSTOM_ACTION_DEF2 = new CustomActionDefinition("Custom Action Label 2", false);

	static {
		TEST_CUSTOM_ACTION_DEF1.setId(1l);
		TEST_CUSTOM_ACTION_DEF2.setId(2l);
	}

	protected static List<CustomActionDefinition> mockCustomActionDefList = new ArrayList<>(Arrays.asList(new CustomActionDefinition[] {TEST_CUSTOM_ACTION_DEF1, TEST_CUSTOM_ACTION_DEF2}));

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		when(customActionDefinitionRepo.findAllByOrderByPositionAsc()).thenReturn(mockCustomActionDefList);
		when(customActionDefinitionRepo.create( any(String.class), any(Boolean.class) )).then(new Answer<CustomActionDefinition>() {
			@Override
			public CustomActionDefinition answer(InvocationOnMock invocation) throws Throwable {
				return createCustomActionDefinition( (String)invocation.getArguments()[0], (Boolean)invocation.getArguments()[1]);
			}
		});

		when(customActionDefinitionRepo.update(any(CustomActionDefinition.class) )).then(new Answer<CustomActionDefinition>() {
			@Override
			public CustomActionDefinition answer(InvocationOnMock invocation) throws Throwable {
				return updateCustomActionDefinition( (CustomActionDefinition)invocation.getArguments()[0]);
			}			
		});
	}

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
		assertEquals(" The created custom action definition does not have the correct label ", TEST_CUSTOM_ACTION_DEF1.isStudentVisible() , createdCustomActionDefinition.isStudentVisible());
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

	@After
	public void cleanUp() {
		customActionDefinitionRepo.deleteAll();
	}
}
