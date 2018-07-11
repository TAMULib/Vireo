package org.tdl.vireo.controller;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.tdl.vireo.model.SubmissionStatus;

import edu.tamu.weaver.response.ApiStatus;

public class SubmissionStatusControllerTest extends AbstractControllerTest {

	@Test
	public void testGetAllSubmissionStatuses() {
		response = submissionStatusController.getAllSubmissionStatuses();
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());

		@SuppressWarnings("unchecked")
		List<SubmissionStatus> submissionStatusList = (List<SubmissionStatus>) response.getPayload().get("ArrayList<SubmissionStatus>");
		assertEquals("There are no submission status present in the list " , mockSubmissionStatusList.size() , submissionStatusList.size());
	}
}
