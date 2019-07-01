package org.tdl.vireo.controller;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.tdl.vireo.Application;
import org.tdl.vireo.model.SubmissionState;
import org.tdl.vireo.model.SubmissionStatus;
import org.tdl.vireo.model.repo.SubmissionStatusRepo;

import edu.tamu.weaver.response.ApiResponse;
import edu.tamu.weaver.response.ApiStatus;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { Application.class })
public class SubmissionStatusControllerTest extends AbstractControllerTest {
    private Logger LOG = LoggerFactory.getLogger(this.getClass());

    private static final SubmissionStatus TEST_SUBMISSION_STATUS1 = new SubmissionStatus("Submission Status Name 1", false, false, false, false, false, false, true, false, SubmissionState.IN_PROGRESS);
    private static final SubmissionStatus TEST_SUBMISSION_STATUS2 = new SubmissionStatus("Submission Status Name 2", false, false, false, false, false, false, false, true, SubmissionState.IN_PROGRESS);
    private static final SubmissionStatus TEST_SUBMISSION_STATUS3 = new SubmissionStatus("Submission Status Name 3", true, false, false, false, false, false, true, true, SubmissionState.IN_PROGRESS);
    private static final SubmissionStatus TEST_SUBMISSION_STATUS4 = new SubmissionStatus("Submission Status Name 4", true, true, true, true, true, true, true, false, SubmissionState.SUBMITTED);
    private static final SubmissionStatus TEST_SUBMISSION_STATUS5 = new SubmissionStatus("Submission Status Name 5", true, true, true, true, true, true, false, true, SubmissionState.APPROVED);

    @Autowired
    private SubmissionStatusRepo submissionStatusRepo;

    @Autowired
    private SubmissionStatusController submissionStatusController;

    @Before
    public void setUp() {
    }

    @Test
    @WithMockUser(roles = {"MANAGER"})
    public void createSubmissionStatus() {
        ApiResponse response = submissionStatusController.createSubmissionStatus(TEST_SUBMISSION_STATUS1);
        assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());

        response = submissionStatusController.createSubmissionStatus(TEST_SUBMISSION_STATUS2);
        assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());

        response = submissionStatusController.createSubmissionStatus(TEST_SUBMISSION_STATUS3);
        assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());

        response = submissionStatusController.createSubmissionStatus(TEST_SUBMISSION_STATUS4);
        assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());

        response = submissionStatusController.createSubmissionStatus(TEST_SUBMISSION_STATUS5);
        assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());

        SubmissionStatus existingSubmission = (SubmissionStatus) response.getPayload().get("SubmissionStatus");
        existingSubmission.setName(existingSubmission.getName() + " (already sent)");

        // should fail when duplicate ID already exists.
        response = submissionStatusController.createSubmissionStatus(existingSubmission);
        assertEquals(ApiStatus.INVALID, response.getMeta().getStatus());
    }

    @Test(expected=AccessDeniedException.class)
    @WithMockUser(roles = {"REVIEWER"})
    public void createSubmissionStatusRestrictedAccess() {
        ApiResponse response = submissionStatusController.createSubmissionStatus(TEST_SUBMISSION_STATUS1);
        assertEquals(ApiStatus.INVALID, response.getMeta().getStatus());
    }

    @Test
    @WithMockUser(roles = {"REVIEWER"})
    public void getAllSubmissionStatuses(){
        ApiResponse response = submissionStatusController.getAllSubmissionStatuses();
        assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
    }

    @Test(expected=AccessDeniedException.class)
    @WithMockUser(roles = {"USER"})
    public void getAllSubmissionStatusesRestrictedAccess() {
        ApiResponse response = submissionStatusController.getAllSubmissionStatuses();
        assertEquals(ApiStatus.INVALID, response.getMeta().getStatus());
    }

    @Test
    @WithMockUser(roles = {"MANAGER"})
    public void getDefault() {
        submissionStatusController.createSubmissionStatus(TEST_SUBMISSION_STATUS1);
        submissionStatusController.createSubmissionStatus(TEST_SUBMISSION_STATUS2);

        ApiResponse response = submissionStatusController.getDefault(SubmissionState.IN_PROGRESS);
        SubmissionStatus defaultSubmissionStatus = (SubmissionStatus) response.getPayload().get("SubmissionStatus");

        assertEquals(defaultSubmissionStatus.getName(), TEST_SUBMISSION_STATUS1.getName());
        assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
    }

    @Test(expected=AccessDeniedException.class)
    @WithMockUser(roles = {"REVIEWER"})
    public void getDefaultRestrictedAccess() {
        ApiResponse response = submissionStatusController.getDefault(SubmissionState.IN_PROGRESS);
        assertEquals(ApiStatus.INVALID, response.getMeta().getStatus());
    }

    @Test
    @WithMockUser(roles = {"MANAGER"})
    public void updateSubmissionStatus() {
        submissionStatusController.createSubmissionStatus(TEST_SUBMISSION_STATUS1);

        SubmissionStatus changingSubmissionStatus = submissionStatusRepo.findByName(TEST_SUBMISSION_STATUS1.getName());

        changingSubmissionStatus.setName(changingSubmissionStatus.getName() + " (updated)");
        changingSubmissionStatus.isActive(!TEST_SUBMISSION_STATUS1.isActive());
        changingSubmissionStatus.isArchived(!TEST_SUBMISSION_STATUS1.isArchived());
        changingSubmissionStatus.isDefault(!TEST_SUBMISSION_STATUS1.isDefault());
        changingSubmissionStatus.isDeletable(!TEST_SUBMISSION_STATUS1.isDeletable());
        changingSubmissionStatus.isEditableByReviewer(!TEST_SUBMISSION_STATUS1.isEditableByReviewer());
        changingSubmissionStatus.isEditableByStudent(!TEST_SUBMISSION_STATUS1.isEditableByStudent());
        changingSubmissionStatus.isPublishable(!TEST_SUBMISSION_STATUS1.isPublishable());
        changingSubmissionStatus.setSubmissionState(SubmissionState.WITHDRAWN);

        ApiResponse response = submissionStatusController.updateSubmissionStatus(changingSubmissionStatus);
        SubmissionStatus updatedSubmissionStatus = (SubmissionStatus) response.getPayload().get("SubmissionStatus");

        assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
        assertEquals(changingSubmissionStatus.getId(), updatedSubmissionStatus.getId());
        assertEquals(changingSubmissionStatus.getName(), updatedSubmissionStatus.getName());
        assertEquals(changingSubmissionStatus.isActive(), updatedSubmissionStatus.isActive());
        assertEquals(changingSubmissionStatus.isArchived(), updatedSubmissionStatus.isArchived());
        assertEquals(changingSubmissionStatus.isDefault(), updatedSubmissionStatus.isDefault());
        assertEquals(changingSubmissionStatus.isDeletable(), updatedSubmissionStatus.isDeletable());
        assertEquals(changingSubmissionStatus.isEditableByReviewer(), updatedSubmissionStatus.isEditableByReviewer());
        assertEquals(changingSubmissionStatus.isEditableByStudent(), updatedSubmissionStatus.isEditableByStudent());
        assertEquals(changingSubmissionStatus.isPublishable(), updatedSubmissionStatus.isPublishable());
        assertEquals(changingSubmissionStatus.getSubmissionState(), updatedSubmissionStatus.getSubmissionState());

        changingSubmissionStatus = submissionStatusRepo.findByName(changingSubmissionStatus.getName());

        changingSubmissionStatus.setName(changingSubmissionStatus.getName() + " (updated)");
        changingSubmissionStatus.isActive(!TEST_SUBMISSION_STATUS1.isActive());
        changingSubmissionStatus.isArchived(!TEST_SUBMISSION_STATUS1.isArchived());
        changingSubmissionStatus.isDefault(!TEST_SUBMISSION_STATUS1.isDefault());
        changingSubmissionStatus.isDeletable(!TEST_SUBMISSION_STATUS1.isDeletable());
        changingSubmissionStatus.isEditableByReviewer(!TEST_SUBMISSION_STATUS1.isEditableByReviewer());
        changingSubmissionStatus.isEditableByStudent(!TEST_SUBMISSION_STATUS1.isEditableByStudent());
        changingSubmissionStatus.isPublishable(!TEST_SUBMISSION_STATUS1.isPublishable());
        changingSubmissionStatus.setSubmissionState(SubmissionState.WITHDRAWN);

        response = submissionStatusController.updateSubmissionStatus(changingSubmissionStatus);
        updatedSubmissionStatus = (SubmissionStatus) response.getPayload().get("SubmissionStatus");

        assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
        assertEquals(changingSubmissionStatus.getId(), updatedSubmissionStatus.getId());
        assertEquals(changingSubmissionStatus.getName(), updatedSubmissionStatus.getName());
        assertEquals(changingSubmissionStatus.isActive(), updatedSubmissionStatus.isActive());
        assertEquals(changingSubmissionStatus.isArchived(), updatedSubmissionStatus.isArchived());
        assertEquals(changingSubmissionStatus.isDefault(), updatedSubmissionStatus.isDefault());
        assertEquals(changingSubmissionStatus.isDeletable(), updatedSubmissionStatus.isDeletable());
        assertEquals(changingSubmissionStatus.isEditableByReviewer(), updatedSubmissionStatus.isEditableByReviewer());
        assertEquals(changingSubmissionStatus.isEditableByStudent(), updatedSubmissionStatus.isEditableByStudent());
        assertEquals(changingSubmissionStatus.isPublishable(), updatedSubmissionStatus.isPublishable());
        assertEquals(changingSubmissionStatus.getSubmissionState(), updatedSubmissionStatus.getSubmissionState());
    }

    @Test
    @WithMockUser(roles = {"MANAGER"})
    public void updateSubmissionStatusInvalidIds() {
        submissionStatusController.createSubmissionStatus(TEST_SUBMISSION_STATUS1);

        SubmissionStatus changingSubmissionStatus = TEST_SUBMISSION_STATUS2;

        changingSubmissionStatus.setId(10000L);
        ApiResponse response = submissionStatusController.updateSubmissionStatus(changingSubmissionStatus);
        assertEquals(ApiStatus.INVALID, response.getMeta().getStatus());
    }

    @Test(expected=AccessDeniedException.class)
    @WithMockUser(roles = {"REVIEWER"})
    public void updateSubmissionStatusRestrictedAccess() {
        SubmissionStatus changingSubmissionStatus = TEST_SUBMISSION_STATUS1;

        changingSubmissionStatus.setId(10000L);
        ApiResponse response = submissionStatusController.updateSubmissionStatus(changingSubmissionStatus);
        assertEquals(ApiStatus.INVALID, response.getMeta().getStatus());
    }

    /* TODO: get this test working and complete it.
    @Test
    @WithMockUser(roles = {"MANAGER"})
    public void deleteSubmissionStatus() {
        submissionStatusController.createSubmissionStatus(TEST_SUBMISSION_STATUS1);
        assertEquals(1, submissionStatusRepo.count());
        submissionStatusController.createSubmissionStatus(TEST_SUBMISSION_STATUS2);
        assertEquals(2, submissionStatusRepo.count());

        // cannot delete default status.
        SubmissionStatus deletingSubmissionStatus = submissionStatusRepo.findByName(TEST_SUBMISSION_STATUS1.getName());
        ApiResponse response = submissionStatusController.deleteSubmissionStatus(deletingSubmissionStatus);
        assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
        assertEquals(2, submissionStatusRepo.count());

        // can delete default status.
        deletingSubmissionStatus = submissionStatusRepo.findByName(TEST_SUBMISSION_STATUS2.getName());
        response = submissionStatusController.deleteSubmissionStatus(deletingSubmissionStatus);
        assertEquals(ApiStatus.SUCCESS, response.getMeta().getStatus());
        assertEquals(1, submissionStatusRepo.count());

        response = submissionStatusController.deleteSubmissionStatus(deletingSubmissionStatus);
        assertEquals(ApiStatus.INVALID, response.getMeta().getStatus());
    }
    */

    @After
    public void cleanUp() {
        submissionStatusRepo.deleteAll();
    }
}
