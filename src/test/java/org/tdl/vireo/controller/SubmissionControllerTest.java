package org.tdl.vireo.controller;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.tdl.vireo.model.Role;
import org.tdl.vireo.model.Submission;

import edu.tamu.weaver.response.ApiStatus;

@SuppressWarnings("unchecked")
public class SubmissionControllerTest extends AbstractControllerTest {
	
	@Test
	public void testGetAllSubmissions() {
		response = submissionController.getAll();
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		List<Submission> submissionList = (List<Submission>) response.getPayload().get("ArrayList<Submission>");
		assertEquals("There are no submissions present in the list " , mockSubmissionList.size() , submissionList.size());
	}

	@Test
	public void testGetAllSubmissionByUser() {
		response = submissionController.getAllByUser(TEST_USER);
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());

		List<Submission> userSubmissionList = (List<Submission>) response.getPayload().get("ArrayList<Submission>");
		assertEquals("There are no submissions present in the list " , mockSubmissionList.size() , userSubmissionList.size());
		assertEquals(" The organization is incorrect " , TEST_ORGANIZATION1 , userSubmissionList.get(1).getOrganization());
		assertEquals(" The submission status is incorrect " , TEST_SUBMISSION_STATUS1 , userSubmissionList.get(1).getSubmissionStatus());
		assertEquals("The submitter does not have the correct email ", TEST_USER.getEmail() , userSubmissionList.get(1).getSubmitter().getEmail());
		assertEquals("The submitter does not have the correct first name ", TEST_USER.getFirstName() , userSubmissionList.get(1).getSubmitter().getFirstName());
	}

	@Test
	public void testGetOneSubmission() throws Exception {
		TEST_USER.setRole(Role.ROLE_ADMIN);
		response = submissionController.getOne(TEST_USER, TEST_SUBMISSION1.getId());
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		Submission submissionById = (Submission) response.getPayload().get("Submission");
		assertEquals(" The submission id is incorrect " , submissionById.getId(), TEST_SUBMISSION1.getId());

		TEST_USER.setRole(Role.ROLE_REVIEWER);
		response = submissionController.getOne(TEST_USER, TEST_SUBMISSION1.getId());
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		Submission submissionBySubmitterAndId = (Submission) response.getPayload().get("Submission");
		assertEquals("The submitter does not have the correct email ", TEST_USER.getEmail() , submissionBySubmitterAndId.getSubmitter().getEmail());
		assertEquals(" The submission id is incorrect " , submissionBySubmitterAndId.getId(), TEST_SUBMISSION1.getId());
	}
}
