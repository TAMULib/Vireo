describe("controller: SubmissionStatusController", function () {

    var compile, controller, q, scope, timeout, SubmissionStatusRepo, WsApi;

    var initializeVariables = function(settings) {
        inject(function ($compile, $q, $timeout, _DragAndDropListenerFactory_, _SubmissionStatusRepo_, _WsApi_) {
            compile = $compile;
            q = $q;
            timeout = $timeout;

            DragAndDropListenerFactory = _DragAndDropListenerFactory_;
            SubmissionStatusRepo = _SubmissionStatusRepo_;
            WsApi = _WsApi_;
        });
    };

    var initializeController = function(settings) {
        inject(function ($controller, $rootScope) {
            scope = $rootScope.$new();

            sessionStorage.role = settings && settings.role ? settings.role : "ROLE_ADMIN";
            sessionStorage.token = settings && settings.token ? settings.token : "faketoken";

            controller = $controller("SubmissionStatusController", {
                $scope: scope,
                $window: mockWindow(),
                DragAndDropListenerFactory: DragAndDropListenerFactory,
                SubmissionStatusRepo: SubmissionStatusRepo,
                WsApi: WsApi
            });

            // ensure that the isReady() is called.
            if (!scope.$$phase) {
                scope.$digest();
            }
        });
    };

    beforeEach(function() {
        module("core");
        module("vireo");
        module("mock.dragAndDropListenerFactory");
        module("mock.modalService");
        module("mock.submissionStatus");
        module("mock.submissionStatusRepo");
        module("mock.wsApi");

        installPromiseMatchers();
        initializeVariables();
        initializeController();
    });

    describe("Is the controller defined", function () {
        it("should be defined", function () {
            expect(controller).toBeDefined();
        });
    });

    describe("Are the scope methods defined", function () {
        it("canRemoveSubmissionStatus should be defined", function () {
            expect(scope.canRemoveSubmissionStatus).toBeDefined();
            expect(typeof scope.canRemoveSubmissionStatus).toEqual("function");
        });
        it("createSubmissionStatus should be defined", function () {
            expect(scope.createSubmissionStatus).toBeDefined();
            expect(typeof scope.createSubmissionStatus).toEqual("function");
        });
        it("invalidDefaultForState should be defined", function () {
            expect(scope.invalidDefaultForState).toBeDefined();
            expect(typeof scope.invalidDefaultForState).toEqual("function");
        });
        it("launchEditModal should be defined", function () {
            expect(scope.launchEditModal).toBeDefined();
            expect(typeof scope.launchEditModal).toEqual("function");
        });
        it("removeSubmissionStatus should be defined", function () {
            expect(scope.removeSubmissionStatus).toBeDefined();
            expect(typeof scope.removeSubmissionStatus).toEqual("function");
        });
        it("resetSubmissionStatus should be defined", function () {
            expect(scope.resetSubmissionStatus).toBeDefined();
            expect(typeof scope.resetSubmissionStatus).toEqual("function");
        });
        it("selectSubmissionStatus should be defined", function () {
            expect(scope.selectSubmissionStatus).toBeDefined();
            expect(typeof scope.selectSubmissionStatus).toEqual("function");
        });
        it("selectSubmissionStatusById should be defined", function () {
            expect(scope.selectSubmissionStatusById).toBeDefined();
            expect(typeof scope.selectSubmissionStatusById).toEqual("function");
        });
        it("selectSubmissionStatusByIndex should be defined", function () {
            expect(scope.selectSubmissionStatusByIndex).toBeDefined();
            expect(typeof scope.selectSubmissionStatusByIndex).toEqual("function");
        });
        it("totalDefaultsForSubmissionState should be defined", function () {
            expect(scope.totalDefaultsForSubmissionState).toBeDefined();
            expect(typeof scope.totalDefaultsForSubmissionState).toEqual("function");
        });
        it("updateSubmissionStatus should be defined", function () {
            expect(scope.updateSubmissionStatus).toBeDefined();
            expect(typeof scope.updateSubmissionStatus).toEqual("function");
        });
    });

    describe("Are the scope.dragControlListeners methods defined", function () {
        it("accept should be defined", function () {
            expect(scope.dragControlListeners.accept).toBeDefined();
            expect(typeof scope.dragControlListeners.accept).toEqual("function");
        });
        it("orderChanged should be defined", function () {
            expect(scope.dragControlListeners.orderChanged).toBeDefined();
            expect(typeof scope.dragControlListeners.orderChanged).toEqual("function");
        });
    });

    describe("Do the scope methods work as expected", function () {
        it("canRemoveSubmissionStatus should return a boolean", function () {
            expect(scope.canRemoveSubmissionStatus()).toBe(false);
            expect(scope.canRemoveSubmissionStatus(false)).toBe(false);
            expect(scope.canRemoveSubmissionStatus("false")).toBe(false);
            expect(scope.canRemoveSubmissionStatus(true)).toBe(true);
            expect(scope.canRemoveSubmissionStatus("true")).toBe(true);
        });
        it("createSubmissionStatus should create a submission status", function () {
            scope.modalData = new mockSubmissionStatus(q);

            spyOn(SubmissionStatusRepo, "create");

            scope.createSubmissionStatus();

            expect(SubmissionStatusRepo.create).toHaveBeenCalled();
        });
        it("invalidDefaultForState should return a boolean", function () {
            var response;
            scope.submissionStatuses = [];

            response = scope.invalidDefaultForState(true, "IN_PROGRESS");
            expect(response).toEqual(false);

            response = scope.invalidDefaultForState(false, "IN_PROGRESS");
            expect(response).toEqual(true);

            scope.submissionStatuses = [
                new mockSubmissionStatus(q),
                new mockSubmissionStatus(q)
            ];

            response = scope.invalidDefaultForState(true, "IN_PROGRESS");
            expect(response).toEqual(false);

            response = scope.invalidDefaultForState(false, "IN_PROGRESS");
            expect(response).toEqual(true);

            scope.submissionStatuses[1].mock(dataSubmissionStatus2);

            response = scope.invalidDefaultForState(true, "IN_PROGRESS");
            expect(response).toEqual(false);

            response = scope.invalidDefaultForState(false, "IN_PROGRESS");
            expect(response).toEqual(false);

            scope.submissionStatuses = [];

            response = scope.invalidDefaultForState(true, "IN_PROGRESS", 1);
            expect(response).toEqual(false);

            response = scope.invalidDefaultForState(false, "IN_PROGRESS", 1);
            expect(response).toEqual(true);

            scope.submissionStatuses = [
                new mockSubmissionStatus(q),
                new mockSubmissionStatus(q)
            ];

            response = scope.invalidDefaultForState(true, "IN_PROGRESS", 1);
            expect(response).toEqual(false);

            response = scope.invalidDefaultForState(false, "IN_PROGRESS", 1);
            expect(response).toEqual(true);

            scope.submissionStatuses[1].mock(dataSubmissionStatus2);

            response = scope.invalidDefaultForState(true, "IN_PROGRESS", 1);
            expect(response).toEqual(false);

            response = scope.invalidDefaultForState(false, "IN_PROGRESS", 1);
            expect(response).toEqual(true);
        });
        it("launchEditModal should open a modal", function () {
            scope.modalData = null;

            spyOn(scope, "resetSubmissionStatus");
            spyOn(scope, "openModal");

            scope.launchEditModal(new mockFieldPredicate(q));

            expect(scope.resetSubmissionStatus).toHaveBeenCalled();
            expect(scope.openModal).toHaveBeenCalled();
            expect(scope.modalData).toBeDefined();
        });
        it("removeSubmissionStatus should remove a submission status", function () {
            scope.modalData = new mockSubmissionStatus(q);

            spyOn(scope.modalData, "delete");

            scope.removeSubmissionStatus();

            expect(scope.modalData.delete).toHaveBeenCalled();
        });
        it("resetSubmissionStatus should reset submission status", function () {
            var submissionStatus = new mockSubmissionStatus(q);

            scope.forms = [];
            scope.modalData = submissionStatus;

            spyOn(scope.submissionStatusRepo, "clearValidationResults");
            spyOn(scope, "closeModal");

            scope.resetSubmissionStatus();

            expect(scope.submissionStatusRepo.clearValidationResults).toHaveBeenCalled();
            expect(scope.closeModal).toHaveBeenCalled();
            expect(scope.modalData).toBeDefined();

            scope.forms.myForm = mockForms();
            scope.resetSubmissionStatus();

            scope.forms.myForm.$pristine = false;
            scope.resetSubmissionStatus();
        });
        it("selectSubmissionStatus should select submission status", function () {
            var submissionStatus = new mockSubmissionStatus(q);

            scope.modalData = undefined;

            spyOn(scope, "resetSubmissionStatus");

            scope.selectSubmissionStatus();
            expect(scope.resetSubmissionStatus).toHaveBeenCalled();
            expect(scope.modalData).not.toBeDefined();

            spyOn(submissionStatus, "refresh");
            scope.selectSubmissionStatus(submissionStatus);
            expect(scope.modalData.id).toBe(submissionStatus.id);
            expect(submissionStatus.refresh).toHaveBeenCalled();

            scope.modalData = undefined;
            submissionStatus.isActive = undefined;

            scope.selectSubmissionStatus(submissionStatus);
            expect(scope.modalData.isActive).toEqual(false);
        });
        it("selectSubmissionStatusById should select submission status", function () {
            scope.modalData = null;
            scope.submissionStatuses = [
                new mockSubmissionStatus(q),
                new mockSubmissionStatus(q)
            ];
            scope.submissionStatuses[0].mock(dataSubmissionStatus2);

            spyOn(scope, "resetSubmissionStatus");

            scope.selectSubmissionStatusById(scope.submissionStatuses[1].id);

            expect(scope.modalData.id).toBe(scope.submissionStatuses[1].id);
            expect(scope.resetSubmissionStatus).toHaveBeenCalled();
        });
        it("selectSubmissionStatusByIndex should select submission status", function () {
            scope.modalData = null;
            scope.submissionStatuses = [
                new mockSubmissionStatus(q),
                new mockSubmissionStatus(q)
            ];
            scope.submissionStatuses[0].mock(dataSubmissionStatus2);

            spyOn(scope, "resetSubmissionStatus");

            scope.selectSubmissionStatusByIndex(1);

            expect(scope.modalData.id).toBe(scope.submissionStatuses[1].id);
            expect(scope.resetSubmissionStatus).toHaveBeenCalled();
        });
        it("totalDefaultsForSubmissionState should return a number", function () {
            var response;
            scope.submissionStatuses = [];

            response = scope.totalDefaultsForSubmissionState("IN_PROGRESS");
            expect(response).toEqual(0);

            scope.submissionStatuses = [
                new mockSubmissionStatus(q),
                new mockSubmissionStatus(q)
            ];
            scope.submissionStatuses[1].mock(dataSubmissionStatus2);

            response = scope.totalDefaultsForSubmissionState("IN_PROGRESS");
            expect(response).toEqual(1);

            scope.submissionStatuses[0].isDefault = false;
            response = scope.totalDefaultsForSubmissionState("IN_PROGRESS");
            expect(response).toEqual(0);

            response = scope.totalDefaultsForSubmissionState("DOES_NOT_EXIST");
            expect(response).toEqual(0);
        });
        it("updateSubmissionStatus should update a submission status", function () {
            scope.modalData = new mockSubmissionStatus(q);

            spyOn(scope.modalData, "save");

            scope.updateSubmissionStatus();

            expect(scope.modalData.save).toHaveBeenCalled();
        });
    });

    describe("Do the scope.dragControlListeners methods work as expected", function () {
        it("accept should create a new submissionStatus", function () {
            var sourceItemHandleScope = {};
            var destSortableScope = {
                element: {
                    0: compile("<div id=\"myId\"></div>")(scope),
                    addClass: function() {}
                }
            };
            var response;

            response = scope.dragControlListeners.accept(sourceItemHandleScope, destSortableScope);

            // TODO: dragControlListeners.accept() always returns false, consider implementation.
            expect(response).toBe(false);

            DragAndDropListenerFactory.listener.dragging = true;
            DragAndDropListenerFactory.listener.trash.id = destSortableScope.element[0].id;
            DragAndDropListenerFactory.listener.trash.element = destSortableScope.element;
            scope.dragControlListeners.accept(sourceItemHandleScope, destSortableScope);
        });
        it("orderChanged should do nothing", function () {
            // method is a stub.
            scope.dragControlListeners.orderChanged();
        });
    });

    describe("Does the scope initialize as expected", function () {
        it("Listen on '/channel/submission-status' should work as expected", function () {
            var submissionStatus1 = new mockSubmissionStatus(q);
            var meta = {
                status: "SUCCESS",
                action: "CREATE",
                message: "Your request was successful"
            };

            SubmissionStatusRepo.listen = function(path, method) {
                var payload = {
                    SubmissionStatus: submissionStatus1
                };
                var data = {
                    meta: meta,
                    payload: payload,
                    status: 200
                };

                method(data);
            };

            spyOn(SubmissionStatusRepo, "reset");

            initializeController();
            scope.$digest();
            timeout.flush();
            expect(SubmissionStatusRepo.reset).toHaveBeenCalled();

            SubmissionStatusRepo.reset = function() {};
            spyOn(SubmissionStatusRepo, "reset");
            meta.action = "UPDATE";

            initializeController();
            scope.$digest();
            timeout.flush();
            expect(SubmissionStatusRepo.reset).toHaveBeenCalled();

            SubmissionStatusRepo.reset = function() {};
            spyOn(SubmissionStatusRepo, "reset");
            meta.action = "DELETE";

            initializeController();
            scope.$digest();
            timeout.flush();
            expect(SubmissionStatusRepo.reset).toHaveBeenCalled();

            SubmissionStatusRepo.reset = function() {};
            spyOn(SubmissionStatusRepo, "reset");
            meta.status = "INVALID";

            initializeController();
            scope.$digest();
            timeout.flush();
            expect(SubmissionStatusRepo.reset).not.toHaveBeenCalled();
        });
    });

});
