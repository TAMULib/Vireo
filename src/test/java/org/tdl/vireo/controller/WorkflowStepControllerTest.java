package org.tdl.vireo.controller;

import static edu.tamu.weaver.response.ApiStatus.ERROR;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.tdl.vireo.model.WorkflowStep;

import edu.tamu.weaver.response.ApiResponse;
import edu.tamu.weaver.response.ApiStatus;

public class WorkflowStepControllerTest  extends AbstractControllerTest {

	@SuppressWarnings("unchecked")
	@Test
	public void testGetAllWorkflowSteps()  {
		response = workflowStepController.getAll();
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		List<WorkflowStep> workkFlowStepList = (List<WorkflowStep>) response.getPayload().get("ArrayList<WorkflowStep>");
		assertEquals("There are no workflowsteps present in the list " , mockWorkflowStepList.size() , workkFlowStepList.size());
	}

	@Test
	public void testGetWorkflowStepById() {
		response  = workflowStepController.getStepById(TEST_WORKFLOW1.getId());
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		WorkflowStep workflowStep = (WorkflowStep) response.getPayload().get("WorkflowStep");
		assertEquals(" The wrong work flow step was returned ", TEST_WORKFLOW1 , workflowStep);
		WorkflowStep wfs = new WorkflowStep("This is a test workflow step");
		wfs.setId(10l);
		response  = workflowStepController.getStepById(wfs.getId());
		assertEquals(ApiStatus.ERROR, response.getMeta().getStatus());
		assertEquals( new ApiResponse(ERROR, "No workflow step for id [" + wfs.getId().toString() + "]").getMeta().getMessage(), response.getMeta().getMessage());
	}

	@Test
	public void testCreateFieldProfile() throws Exception {
		TEST_WORKFLOW1.setOriginatingOrganization(TEST_ORGANIZATION2);
		response  = workflowStepController.createFieldProfile(TEST_ORGANIZATION1.getId(), TEST_WORKFLOW1.getId(), TEST_FIELD_PROFILE1);
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		
		TEST_WORKFLOW1.setOriginatingOrganization(TEST_ORGANIZATION1);
		response  = workflowStepController.createFieldProfile(TEST_ORGANIZATION1.getId(), TEST_WORKFLOW1.getId(), TEST_FIELD_PROFILE1);
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
	}

	@Test
	public void testUpdateFieldProfile() throws Exception {
		TEST_WORKFLOW1.setAggregateFieldProfiles(mockFieldProfileList);
		TEST_WORKFLOW1.setOriginatingOrganization(TEST_ORGANIZATION1);
		response = workflowStepController.updateFieldProfile(TEST_ORGANIZATION1.getId(), TEST_WORKFLOW1.getId(), TEST_FIELD_PROFILE1);
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
	}

	@Test
	public void testRemoveFieldProfile() throws Exception {//TODO
		TEST_WORKFLOW1.addOriginalFieldProfile(TEST_FIELD_PROFILE1);
		response = workflowStepController.removeFieldProfile(TEST_ORGANIZATION1.getId(), TEST_WORKFLOW1.getId(), TEST_FIELD_PROFILE1);
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
	}

	@Test
	public void testReorderFieldProfiles() throws Exception { //TODO
		Integer val1 = 2;
		Integer val2 = 3;
		response = workflowStepController.reorderFieldProfiles(TEST_ORGANIZATION1.getId(), TEST_WORKFLOW1.getId(),val1, val2);
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
	}

	@Test
	public void testAddNote() throws Exception {//TODO
		response = workflowStepController.addNote(TEST_ORGANIZATION2.getId(), TEST_WORKFLOW1.getId(), TEST_NOTE1);
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
	}

	@Test
	public void testUpdateNote() throws Exception {//TODO
		response = workflowStepController.updateNote(TEST_ORGANIZATION2.getId(), TEST_WORKFLOW1.getId(), TEST_NOTE1);
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
	}

	@Test
	public void testRemoveNote() throws Exception {//TODO
		response = workflowStepController.removeNote(TEST_ORGANIZATION1.getId(), TEST_WORKFLOW1.getId(), TEST_NOTE1);
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
	}

	@Test
	public void testReorderNotes() throws Exception {
		Integer val1 = 2;
		Integer val2 = 3;
		response = workflowStepController.reorderNotes(TEST_ORGANIZATION1.getId(), TEST_WORKFLOW1.getId(),val1, val2);
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
	}
}
