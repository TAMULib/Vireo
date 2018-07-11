package org.tdl.vireo.controller;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.tdl.vireo.model.Organization;
import org.tdl.vireo.model.WorkflowStep;

import edu.tamu.weaver.response.ApiStatus;

public class OrganizationControllerTest extends AbstractControllerTest {

	@SuppressWarnings("unchecked")
	@Test
	public void testAllOrganizations() {
		response = organizationController.allOrganizations();
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		List<Organization> organizationList = (List<Organization>) response.getPayload().get("ArrayList<Organization>");
		assertEquals("There are no degrees in the list ", mockOrganizationList.size() , organizationList.size());
	}

	@Test
	public void testGetOrganization() {
		response = organizationController.getOrganization(TEST_ORGANIZATION1.getId());
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		Organization organizationById = (Organization) response.getPayload().get("Organization");
		assertEquals(" The organization returned does not have the correct id" , TEST_ORGANIZATION1.getId() , organizationById.getId());
	}

	@Test
	public void testCreateOrganization() {
		response = organizationController.createOrganization(TEST_ORGANIZATION1.getId(), TEST_ORGANIZATION1);
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		Organization createdOrganization = (Organization) response.getPayload().get("Organization");
		assertEquals(" The organization created does not have the correct name" , TEST_ORGANIZATION1.getName() , createdOrganization.getName());
		assertEquals(" The organization created does not have the correct org category name " , TEST_ORGANIZATION1.getCategory().getName() , createdOrganization.getCategory().getName());
	}

	@Test
	public void testUpdateOrganization() {
		TEST_ORGANIZATION1.setName("Modified Org Name");
		TEST_ORGANIZATION1.setCategory(TEST_ORGANIZATION_CATEGORY2);
		response = organizationController.updateOrganization(TEST_ORGANIZATION1);
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		Organization updatedOrganization = (Organization) response.getPayload().get("Organization");
		assertEquals(" The organization does not have the updated name" , TEST_ORGANIZATION1.getName() , updatedOrganization.getName());
		assertEquals(" The organization created does not have the updated org category " , TEST_ORGANIZATION2.getCategory().getName() , updatedOrganization.getCategory().getName());
	}

	@Test
	public void testDeleteOrganization() {
		response = organizationController.deleteOrganization(TEST_ORGANIZATION2);
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		assertEquals(response.getMeta().getMessage(), "Organization Organization2_name has been deleted!" );
	}

	@Test
	public void testRestoreOrganizationDefaults() {
		response = organizationController.restoreOrganizationDefaults(TEST_ORGANIZATION2);
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		assertEquals(response.getMeta().getMessage(), "Organization Organization2_name has been restored to defaults!" );
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testGetWorkflowStepsForOrganization() {
		TEST_ORGANIZATION1.setAggregateWorkflowSteps(mockWorkflowStepList);
		response = organizationController.getWorkflowStepsForOrganization(TEST_ORGANIZATION1.getId());
		List<WorkflowStep> workflowStepList  = (List<WorkflowStep>)response.getPayload().get("ArrayList<WorkflowStep>");
		assertEquals(" The size of workflowStep is incorrect ", workflowStepList.size() , TEST_ORGANIZATION1.getAggregateWorkflowSteps().size());
	}

	@Test
	public void testCreateWorkflowStepsForOrganization() throws Exception {
		response = organizationController.createWorkflowStepsForOrganization(TEST_ORGANIZATION1.getId(), TEST_WORKFLOW1);
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		WorkflowStep createdWorkflowStep = (WorkflowStep) response.getPayload().get("WorkflowStep");
		assertEquals(" The created workflow does not have the correct name" , TEST_WORKFLOW1.getName() , createdWorkflowStep.getName());
		assertEquals(" The created workflow does not have the correct originating organization" , TEST_WORKFLOW1.getOriginatingOrganization() , createdWorkflowStep.getOriginatingOrganization());
	}

	@Test
	public void testUpdateWorkflowStepsForOrganization() throws Exception {
		//TODO
		response = organizationController.updateWorkflowStepsForOrganization(TEST_ORGANIZATION1.getId(), TEST_WORKFLOW1);
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
	}

	@Test
	public void testDeleteWorkflowStep() throws Exception {
		//TODO
	}

	@Test
	public void testShiftWorkflowStepUp() {
		//TODO
	}

	@Test
	public void testShiftWorkflowStepDown() {
		//TODO
	}

	@Test
	public void testAddEmailWorkflowRule() {
		//TODO
		//Map<String, Object> data = new HashMap<String, Object>();
		//data.put("submissionStatusId",TEST_SUBMISSION_STATUS1.getId().intValue());
		//data.put("recipient", TEST_ASSIGNEE_RECIPIENT);
		//data.put("templateId", TEST_EMAIL_TEMPLATE1.getId().intValue());
		//response = organizationController.addEmailWorkflowRule(TEST_ORGANIZATION1.getId(), data);
	}

	@Test
	public void testEditEmailWorkflowRule() {
		//TODO
	}

	@Test
	public void testRemoveEmailWorkflowRule() {
		//TODO
	}

	@Test
	public void testChangeEmailWorkflowRuleActivation() {
		//TODO
	}

}
