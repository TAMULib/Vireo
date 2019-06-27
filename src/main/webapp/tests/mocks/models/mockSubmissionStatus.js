var dataSubmissionStatus1 = {
    id: 1,
    name: "submission status 1",
    isActive: false,
    isArchived: false,
    isDefault: true,
    isDeletable: false,
    isEditableByReviewer: false,
    isEditableByStudent: false,
    isPublishable: false,
    submissionState: "IN_PROGRESS"
};

var dataSubmissionStatus2 = {
    id: 2,
    name: "submission status 2",
    isActive: false,
    isArchived: false,
    isDefault: true,
    isDeletable: false,
    isEditableByReviewer: false,
    isEditableByStudent: false,
    isPublishable: false,
    submissionState: "SUBMITTED"
};

var dataSubmissionStatus3 = {
    id: 3,
    name: "submission status 3",
    isActive: false,
    isArchived: false,
    isDefault: true,
    isDeletable: false,
    isEditableByReviewer: false,
    isEditableByStudent: false,
    isPublishable: false,
    submissionState: "WITHDRAWN"
};

var dataSubmissionStatus4 = {
    id: 4,
    name: "submission status 4",
    isActive: false,
    isArchived: true,
    isDefault: false,
    isDeletable: false,
    isEditableByReviewer: false,
    isEditableByStudent: false,
    isPublishable: false,
    submissionState: "IN_PROGRESS"
};

var dataSubmissionStatus5 = {
    id: 5,
    name: "submission status 5",
    isActive: false,
    isArchived: true,
    isDefault: false,
    isDeletable: false,
    isEditableByReviewer: false,
    isEditableByStudent: false,
    isPublishable: false,
    submissionState: "SUBMITTED"
};

var dataSubmissionStatus6 = {
    id: 6,
    name: "submission status 6",
    isActive: false,
    isArchived: true,
    isDefault: false,
    isDeletable: false,
    isEditableByReviewer: false,
    isEditableByStudent: false,
    isPublishable: false,
    submissionState: "WITHDRAWN"
};

var mockSubmissionStatus = function($q) {
    var model = mockModel("SubmissionStatus", $q, dataSubmissionStatus1);

    return model;
};

angular.module("mock.submissionStatus", []).service("SubmissionStatus", mockSubmissionStatus);

