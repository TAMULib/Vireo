package org.tdl.vireo.model.repo.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.tdl.vireo.exception.SubmissionStatusException;
import org.tdl.vireo.model.SubmissionState;
import org.tdl.vireo.model.SubmissionStatus;
import org.tdl.vireo.model.repo.SubmissionStatusRepo;
import org.tdl.vireo.model.repo.custom.SubmissionStatusRepoCustom;

import edu.tamu.weaver.data.model.repo.impl.AbstractWeaverRepoImpl;

public class SubmissionStatusRepoImpl extends AbstractWeaverRepoImpl<SubmissionStatus, SubmissionStatusRepo> implements SubmissionStatusRepoCustom {

    @Autowired
    private SubmissionStatusRepo submissionStatusRepo;

    @Override
    public SubmissionStatus create(SubmissionStatus submissionStatus) {
        updateDefaultsToFalse(submissionStatus);
        return super.create(submissionStatus);
    }

    @Override
    public SubmissionStatus create(String name, Boolean archived, Boolean publishable, Boolean deletable, Boolean editableByReviewer, Boolean editableByStudent, Boolean active, Boolean isDefault, Boolean clearApproval, SubmissionState submissionState, List<SubmissionStatus> transitionSubmissionStatuses) {
        return submissionStatusRepo.create(new SubmissionStatus(name, archived, publishable, deletable, editableByReviewer, editableByStudent, active, isDefault, clearApproval, submissionState == null ? SubmissionState.NONE : submissionState, transitionSubmissionStatuses));
    }

    @Override
    public SubmissionStatus update(SubmissionStatus submissionStatus) {
        updateDefaultsToFalse(submissionStatus);
        return super.update(submissionStatus);
    }

    @Override
    public void delete(SubmissionStatus submissionStatus) {
        submissionStatusRepo.deletePreserveDefault(submissionStatus.getId());
    }

    @Override
    protected String getChannel() {
        return "/channel/submission-status";
    }

    private void updateDefaultsToFalse(SubmissionStatus submissionStatus) {
        if (submissionStatus.getSubmissionState() != SubmissionState.NONE) {
            if (submissionStatus.isDefault()) {
                if (submissionStatus.getId() == null) {
                    submissionStatusRepo.updateDefaultsToFalse(submissionStatus.getSubmissionState());
                }
                else {
                    submissionStatusRepo.updateDefaultsToFalse(submissionStatus.getSubmissionState(), submissionStatus.getId());
                }
            }
            else {
                SubmissionStatus defaultStatus = submissionStatusRepo.findBySubmissionStateAndIsDefaultTrue(submissionStatus.getSubmissionState());
                if (defaultStatus == null || defaultStatus.getId() == submissionStatus.getId()) {
                    throw new SubmissionStatusException("Cannot save Submission Status " + submissionStatus.getName() + ", the " + submissionStatus.getSubmissionState().name() + " state must have a default.");
                }
            }
        }
    }

}
