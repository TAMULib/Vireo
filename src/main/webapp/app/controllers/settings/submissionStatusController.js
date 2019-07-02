vireo.controller("SubmissionStatusController", function ($controller, $scope, $q, ApiResponseActions, SubmissionStates, SubmissionStatusRepo, DragAndDropListenerFactory) {

    angular.extend(this, $controller("AbstractController", {
        $scope: $scope
    }));

    $scope.submissionStates = SubmissionStates;

    $scope.submissionStatusRepo = SubmissionStatusRepo;

    $scope.submissionStatuses = $scope.submissionStatusRepo.getAll();

    $scope.ready = $q.all([$scope.submissionStatusRepo.ready()]);

    $scope.dragging = false;

    $scope.trashCanId = "submission-status-trash";

    $scope.forms = {};

    $scope.toggleOptions = [{
        "true": "Yes"
    }, {
        "false": "No"
    }];

    $scope.ready.then(function () {

        $scope.resetSubmissionStatus = function () {
            $scope.submissionStatusRepo.clearValidationResults();

            for (var key in $scope.forms) {
                if (angular.isDefined($scope.forms[key]) && !$scope.forms[key].$pristine) {
                    $scope.forms[key].$setPristine();
                    $scope.forms[key].$setUntouched();
                }
            }

            $scope.modalData = {
                isArchived: false,
                isPublishable: false,
                isDeletable: false,
                isEditableByReviewer: false,
                isEditableByStudent: false,
                isActive: false,
                isDefault: false,
                clearApproval: false,
                submissionState: $scope.submissionStates.NONE,
                transitionSubmissionStatuses: []
            };

            $scope.closeModal();
        };

        $scope.resetSubmissionStatus();

        $scope.createSubmissionStatus = function () {
            $scope.submissionStatusRepo.create($scope.modalData);
        };

        $scope.selectSubmissionStatus = function (submissionStatus) {
            $scope.resetSubmissionStatus();
            if (submissionStatus) {
                $scope.modalData = submissionStatus;
                $scope.modalData.refresh();

                // TODO: improve handling of isActive = NULL because it can be a boolean or a NULL in the database.
                if ($scope.modalData.isActive !== true && $scope.modalData.isActive !== false) {
                    $scope.modalData.isActive = false;
                }
            }
        };

        $scope.selectSubmissionStatusByIndex = function (index) {
            $scope.selectSubmissionStatus($scope.submissionStatuses[index]);
        };

        $scope.selectSubmissionStatusById = function (id) {
            var submissionStatus;
            for (var key in $scope.submissionStatuses) {
                if ($scope.submissionStatuses[key].id === id) {
                    submissionStatus = $scope.submissionStatuses[key];
                    break;
                }
            }

            $scope.selectSubmissionStatus(submissionStatus);
        };

        $scope.launchEditModal = function (id) {
            $scope.selectSubmissionStatusById(id);
            $scope.openModal("#submissionStatusEditModal");
        };

        $scope.updateSubmissionStatus = function () {
            $scope.modalData.save();
        };

        $scope.removeSubmissionStatus = function () {
            $scope.modalData.delete();
        };

        $scope.dragControlListeners = DragAndDropListenerFactory.buildDragControls({
            trashId: $scope.trashCanId,
            dragging: $scope.dragging,
            select: $scope.selectSubmissionStatusByIndex,
            model: $scope.submissionStatuses,
            confirm: "#submissionStatusConfirmRemoveModal",
            reorder: function() {},
            container: "#submission-statuses"
        });

        var listener = $scope.dragControlListeners.getListener();

        $scope.dragControlListeners.accept = function (sourceItemHandleScope, destSortableScope) {
            var currentElement = destSortableScope.element;

            if (listener.dragging && currentElement[0].id === listener.trash.id) {
                listener.trash.hover = true;
                listener.trash.element = currentElement;
                listener.trash.element.addClass("dragging");
            } else {
                listener.trash.hover = false;
            }

            return false;
        };


        $scope.dragControlListeners.orderChanged = function (event) {};

        $scope.totalDefaultsForSubmissionState = function (submissionState, submissionId) {
            var total = 0;

            for (var key in $scope.submissionStatuses) {
                if ($scope.submissionStatuses[key].submissionState === submissionState) {
                    if ($scope.submissionStatuses[key].isDefault === true) {
                        if (!angular.isDefined(submissionId) || $scope.submissionStatuses[key].id !== submissionId) {
                            total++;
                        }
                    }
                }
            }

            return total;
        };

        $scope.invalidDefaultForState = function(isDefault, submissionState, submissionId) {
            if (isDefault === true || isDefault === "true") {
                return false;
            }

            var id = submissionId;
            if (angular.isDefined(submissionId)) {
                id = parseInt(submissionId);
            }

            return $scope.totalDefaultsForSubmissionState(submissionState, id) !== 1;
        };

        $scope.submissionStatusRepo.listen([ApiResponseActions.CREATE, ApiResponseActions.UPDATE, ApiResponseActions.DELETE], function (data) {
            // reset the repo to ensure secondary changes are also presented.
            if (data.meta.status === "SUCCESS") {
                $scope.submissionStatusRepo.reset();
                $scope.resetSubmissionStatus();
            }
        });

    });
});
