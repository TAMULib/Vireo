vireo.repo("SubmissionStatusRepo", function SubmissionStatusRepo(WsApi) {

	var submissionStatusRepo = this;

	// additional repo methods and variables

	submissionStatusRepo.findById = function(id) {
		var foundStatus = null;
		angular.forEach(submissionStatusRepo.getAll(), function(status) {
			if(status.id === id) foundStatus = status;
		});
		return foundStatus;
	};

	submissionStatusRepo.findByName = function(name) {
		var foundStatus = null;
		angular.forEach(submissionStatusRepo.getAll(), function(status) {
			if(status.name == name) foundStatus = status;
		});
		return foundStatus;
	};

    submissionStatusRepo.getDefault = function(state) {
        angular.extend(submissionStatusRepo.mapping.default, {
            'method': 'default/' + state
        });

        return WsApi.fetch(submissionStatusRepo.mapping.default);
    };

	return submissionStatusRepo;

});
