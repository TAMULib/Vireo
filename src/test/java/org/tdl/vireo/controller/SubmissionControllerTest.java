package org.tdl.vireo.controller;

import static edu.tamu.weaver.response.ApiStatus.ERROR;
import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.tdl.vireo.model.ActionLog;
import org.tdl.vireo.model.CustomActionValue;
import org.tdl.vireo.model.Role;
import org.tdl.vireo.model.Submission;
import org.tdl.vireo.model.User;

import edu.tamu.weaver.response.ApiResponse;
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
	public void testGetAllSubmissionByUser() throws Exception {
		response = submissionController.getAllByUser(TEST_USER);
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());

		List<Submission> userSubmissionList = (List<Submission>) response.getPayload().get("ArrayList<Submission>");
		assertEquals("There are no submissions present in the list " , mockSubmissionList.size() , userSubmissionList.size());
		assertEquals(" The organization is incorrect " , TEST_ORGANIZATION1 , userSubmissionList.get(1).getOrganization());
		assertEquals("The submitter does not have the correct email ", TEST_USER.getEmail() , userSubmissionList.get(1).getSubmitter().getEmail());
		assertEquals("The submitter does not have the correct first name ", TEST_USER.getFirstName() , userSubmissionList.get(1).getSubmitter().getFirstName());
	}

	@Test
	public void testGetOneSubmission() {
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

	@Test
	public void testGetSubmissionByAdvisorHash() {
		Integer advisorHashCode = "TEST_ADVISOR".hashCode();
		TEST_SUBMISSION1.setAdvisorAccessHash(advisorHashCode.toString());

		response = submissionController.getOne(advisorHashCode.toString());
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		Submission advisorHashSubmission = (Submission) response.getPayload().get("Submission");
		assertEquals(" The submission contains the wrong advisor hash " , TEST_SUBMISSION1.getAdvisorAccessHash() , advisorHashSubmission.getAdvisorAccessHash());
	}

	@Test
	public void testCreateSubmission() throws Exception {
		Map<String, String> data = new HashMap<String, String>();
        data.put("organizationId", TEST_ORGANIZATION1.getId().toString());
		response = submissionController.createSubmission(TEST_USER, TEST_CREDENTIALS, data);
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		Submission createdSubmission = (Submission) response.getPayload().get("Submission");
		assertEquals(" The created submission has the wrong organization ", TEST_ORGANIZATION1.getName() , createdSubmission.getOrganization().getName());
		assertEquals(" The created submission does not have the correct submitter ", TEST_USER, createdSubmission.getSubmitter());
	}

	@Test
	public void testDeleteSubmission() {
		TEST_USER3.setRole(Role.ROLE_ANONYMOUS);
		response = submissionController.deleteSubmission(TEST_USER3, TEST_SUBMISSION1.getId());
		assertEquals(ApiStatus.ERROR, response.getMeta().getStatus());
		assertEquals("Insufficient permisions to delete this submission.", response.getMeta().getMessage());

		TEST_USER3.setRole(Role.ROLE_ADMIN);
		response = submissionController.deleteSubmission(TEST_USER3, TEST_SUBMISSION1.getId());
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
	}

	@Test
	public void testAddComment() {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("commentVisiblity", "private");
		data.put("subject", "subject");
		data.put("message", "message");
		response = submissionController.addComment(TEST_USER, TEST_SUBMISSION1.getId(), data);
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());

		data.put("commentVisiblity", "public");
		data.put("sendEmailToRecipient", true);
		data.put("sendEmailToCCRecipient", true);
		data.put("recipientEmail", "rmathew@tamu.edu");
		data.put("ccRecipientEmail", "rmathew@tamu.edu");
		response = submissionController.addComment(TEST_USER, TEST_SUBMISSION1.getId(), data);
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
	}

	@Test
	public void testSendEmail() {
		Map<String, String> settings = new HashMap<String, String>();
		settings.put("displayName", "aggieJack");
		settings.put("ccEmail", "rmathew@tamu.edu");
		settings.put("preferedEmail", "rmathew@tamu.edu");
		TEST_USER.setSettings(settings);
		TEST_SUBMISSION1.setSubmitter(TEST_USER);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("message", "message");
		data.put("commentVisiblity", "private");
		data.put("sendEmailToRecipient", true);
		data.put("sendEmailToCCRecipient", true);
		data.put("recipientEmail", "rmathew@tamu.edu");
		data.put("ccRecipientEmail", "rmathew@tamu.edu");
		response = submissionController.sendEmail(TEST_USER, TEST_SUBMISSION1.getId(), data);
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
	}

	@Test
	public void testBatchComment() {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("sendEmailToRecipient", true);
		data.put("sendEmailToCCRecipient", true);
		data.put("recipientEmail", "rmathew@tamu.edu");
		response = submissionController.batchComment(TEST_USER, data);
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
	}

	@Test
	public void testUpdateFieldValue() {
		//TODO
	}

	@Test
	public void testUpdateCustomActionValue() {
		TEST_SUBMISSION1.addCustomActionValue(TEST_CUSTOM_ACTION_VALUE1);
		TEST_SUBMISSION1.editCustomActionValue(TEST_CUSTOM_ACTION_VALUE1).setDefinition(TEST_CUSTOM_ACTION_DEF2);
		TEST_SUBMISSION1.editCustomActionValue(TEST_CUSTOM_ACTION_VALUE1).setValue(false);
		response = submissionController.updateCustomActionValue(TEST_SUBMISSION1.getId(), TEST_CUSTOM_ACTION_VALUE1);
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		CustomActionValue updatedCustomActionValue = (CustomActionValue) response.getPayload().get("CustomActionValue");
		assertEquals(" The updatedCustomActionValue does not have updated custom action def " ,TEST_CUSTOM_ACTION_DEF2.getLabel(), updatedCustomActionValue.getDefinition().getLabel() );
		assertEquals(" The updatedCustomActionValue does not have updated value " ,TEST_CUSTOM_ACTION_VALUE1.getValue(), updatedCustomActionValue.getValue() );
	}

	@Test
	public void testChangeStatus() {
		response = submissionController.changeStatus(TEST_USER, TEST_SUBMISSION1.getId(), "Modified Submission Status");
		assertEquals(ApiStatus.ERROR, response.getMeta().getStatus());
		assertEquals(" The submission status was not changed ", "Could not find a submission status name Modified Submission Status", response.getMeta().getMessage());

		TEST_SUBMISSION_STATUS1.setName("Updated Status Name");
		TEST_SUBMISSION1.setSubmissionStatus(TEST_SUBMISSION_STATUS1);
		response = submissionController.changeStatus(TEST_USER, TEST_SUBMISSION1.getId(), TEST_SUBMISSION_STATUS1.getName());
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		Submission submission = (Submission) response.getPayload().get("Submission");
		assertEquals(" The submission status has not been changed ", TEST_SUBMISSION1.getSubmissionStatus().getName(), submission.getSubmissionStatus().getName());
	}

	@Test
	public void testBatchUpdateSubmissionStatuses() {
		TEST_SUBMISSION_STATUS1.setName("Updated Status Name");
		TEST_SUBMISSION1.setSubmissionStatus(TEST_SUBMISSION_STATUS1);
		response = submissionController.batchUpdateSubmissionStatuses(TEST_USER, TEST_SUBMISSION_STATUS1.getName());
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
	}

	@Test
	public void testPublish() {
		//TODO
		//response = submissionController.publish(TEST_USER, TEST_SUBMISSION1.getId(), depositLocationId)
	}

	@Test
	public void testBatchExportWithFilter() {
		//TODO
		//response = submissionController.batchExportWithFilter(response, user, packagerName, filterId);
	}

	@Test
	public void testBatchExport() {
		//TODO
		//response = submissionController.batchExport(httpServletResponse, user, packagerName);
	}

	@Test
	public void testBatchAssignTo() throws Exception {
		//TODO
		TEST_SUBMISSION1.setAssignee(TEST_USER2);
		response = submissionController.batchAssignTo(TEST_USER, TEST_USER2);
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
	}

	@Test
	public void testBatchPublish() throws Exception {
		//TODO - add further test cases for different conditions
		response = submissionController.batchPublish(TEST_USER, 1l);
		assertEquals(ApiStatus.ERROR, response.getMeta().getStatus());
		assertEquals("Could not find a submission status name Published", response.getMeta().getMessage());
	}

	@Test
	public void testSubmitDate() throws Exception {
		//TODO
	}

	@Test
	public void testAassign() {
		TEST_SUBMISSION1.setAssignee(TEST_USER2);
		response = submissionController.assign(TEST_USER, TEST_SUBMISSION1.getId(), TEST_SUBMISSION1.getAssignee());
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());

		TEST_SUBMISSION1.setAssignee(new User());
		response = submissionController.assign(TEST_USER, TEST_SUBMISSION1.getId(), TEST_SUBMISSION1.getAssignee());
		assertEquals(ApiStatus.ERROR, response.getMeta().getStatus());
		assertEquals("The response was successful ", new ApiResponse(ERROR, "Could not find a assignee!").getMeta().getMessage() , response.getMeta().getMessage());
	}

	@Test
	public void testRemoveFieldValue() {
		TEST_SUBMISSION1.addFieldValue(TEST_FIELD_VALUE2);
		response = submissionController.removeFieldValue(TEST_SUBMISSION1.getId(), TEST_FIELD_VALUE2);
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		Submission submission = (Submission) response.getPayload().get("Submission");
		assertEquals("The fieldValue was not removed ", null , submission.getFieldValues());
	}

	@Test
	public void testUpdateReviewerNotes() {
		TEST_SUBMISSION1.setReviewerNotes("reviewerNotes for a Submission");
		Map<String, String> requestData = new HashMap<String, String>();
		requestData.put("reviewerNotes", "reviewerNotes for a Submission");

		response = submissionController.updateReviewerNotes(TEST_USER, TEST_SUBMISSION1.getId(), requestData);
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		Submission submission = (Submission) response.getPayload().get("Submission");
		assertEquals("The submission does not have the reviewer notes ",  TEST_SUBMISSION1.getReviewerNotes(), submission.getReviewerNotes());
	}

	@Test
	public void testSetSubmissionNeedsCorrection() {
		TEST_SUBMISSION_STATUS2.setName("Needs Correction" );
		TEST_SUBMISSION2.setSubmissionStatus(TEST_SUBMISSION_STATUS2);
		response = submissionController.setSubmissionNeedsCorrection(TEST_USER, TEST_SUBMISSION2.getId());
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		Submission submission = (Submission) response.getPayload().get("Submission");
		assertEquals("The submission does not have the correct submission status ",  TEST_SUBMISSION2.getSubmissionStatus().getName(),  submission.getSubmissionStatus().getName());
	}

	@Test
	public void testSetSubmissionCorrectionsReceived() {
		TEST_SUBMISSION_STATUS2.setName("Corrections Received");
		TEST_SUBMISSION2.setSubmissionStatus(TEST_SUBMISSION_STATUS2);
		response = submissionController.setSubmissionCorrectionsReceived(TEST_USER, TEST_SUBMISSION2.getId());
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		Submission submission = (Submission) response.getPayload().get("Submission");
		assertEquals("The submission does not have the correct corrections recieved submission status ",  TEST_SUBMISSION2.getSubmissionStatus().getName(),  submission.getSubmissionStatus().getName());
	}

	@Test
	public void testAddMessage() throws Exception {
		ActionLog submissionPublicLog = new ActionLog(TEST_SUBMISSION_STATUS1, TEST_USER, Calendar.getInstance(), "This is a test message for a Submmsion", false);
		response = submissionController.addMessage(TEST_USER, TEST_SUBMISSION1.getId(), submissionPublicLog.getEntry());
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		ActionLog responseActionLog = (ActionLog) response.getPayload().get("ActionLog");
		assertEquals("The action log does not have the correct entry ", submissionPublicLog.getEntry(), responseActionLog.getEntry());
	}

	@Test
	public void testQuerySubmission() {
		//TODO
		//response = submissionController.querySubmission(user, page, size)
	}

	@Test
	public void testSubmissionFile() {
		//TODO
		//response =submissionController.submissionFile(response, uri);
	}

	@Test
	public void testUploadFile() {
		//TODO response = submissionController.uploadFile(TEST_USER, submissionId, documentType, file);
	}

	@Test
	public void testRenameFile() {
		//TODO response = submissionController.renameFile(user, submissionId, documentType, requestData)
	}

	@Test
	public void testRemoveFile() throws Exception {
		//TODO
		TEST_USER.setRole(Role.ROLE_ADMIN);
		TEST_FIELD_VALUE1.setFieldPredicate(TEST_FIELD_PREDICATE1);
		//response = submissionController.removeFile(TEST_USER, TEST_SUBMISSION1.getId(), TEST_FIELD_VALUE1.getId());
	}

	@Test
	public void testArchiveFile() {
		//TODO		reponse = submissionController.archiveFile(TEST_USER, submissionId, documentType, requestData);
	}

	@Test
	public void testSendAdvisorEmail() {
		//TODO
		TEST_EMAIL_TEMPLATE1.setName("SYSTEM Advisor Review Request");
		TEST_EMAIL_TEMPLATE1.setSystemRequired(true);
		response = submissionController.sendAdvisorEmail(TEST_USER, TEST_SUBMISSION1.getId());
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
	}

	@Test
	public void testUpdateAdvisorApproval() {
		TEST_FIELD_VALUE1.setContacts(mockContactList);
		TEST_SUBMISSION1.setFieldValues(mockFieldValueSet);
		TEST_SUBMISSION1.setApproveAdvisor(true);
		TEST_SUBMISSION1.setApproveAdvisorDate(Calendar.getInstance());
		TEST_SUBMISSION1.setApproveEmbargo(true);
		TEST_SUBMISSION1.setApproveEmbargoDate(Calendar.getInstance());

		Map<String, Object> embargo = new HashMap<String, Object>();
		embargo.put("embargo", TEST_EMBARGO1);
		embargo.put("approve",true);
		embargo.put("clearApproval",false);

		Map<String, Object> advisor = new HashMap<String, Object>();
		advisor.put("advisor", TEST_SUBMISSION1.getFieldValues());
		advisor.put("approve",true);
		advisor.put("clearApproval",false);

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("embargo", embargo);
		data.put("advisor", advisor);
		data.put("message", " This is test message for updating the advisor approval");

		response = submissionController.updateAdvisorApproval(TEST_SUBMISSION1.getId(), data);
		assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
		Submission submission = (Submission) response.getPayload().get("Submission");
		assertEquals("The advisor approval is not updated ", TEST_SUBMISSION1.getApproveAdvisor(), submission.getApproveAdvisor());
		assertEquals("The advisor approval date is not updated ", TEST_SUBMISSION1.getApproveAdvisorDate(), submission.getApproveAdvisorDate());
		assertEquals("The embargo approval is not updated ", TEST_SUBMISSION1.getApproveEmbargo(), submission.getApproveEmbargo());
		assertEquals("The embargo approval date is not updated ", TEST_SUBMISSION1.getApproveEmbargoDate(), submission.getApproveEmbargoDate());
	}
}
