vireo.service("OrganizationRepo", function($route, $q, WsApi, AbstractModel) {

	var self;
	
	var OrganizationRepo = function(futureData) {
		self = this;

		//This causes our model to extend AbstractModel
		angular.extend(self, AbstractModel);
		
		self.unwrap(self, futureData);
		
	};

	var workflowStepsCache = [];
	
	OrganizationRepo.data = null;
	
	OrganizationRepo.listener = null;

	OrganizationRepo.promise = null;

	OrganizationRepo.newOrganization = {};

	OrganizationRepo.resetNewOrganization = function() {
		for(var key in OrganizationRepo.newOrganization) {
			delete OrganizationRepo.newOrganization[key];
		}
	};

	OrganizationRepo.getNewOrganization = function() {
		return OrganizationRepo.newOrganization;
	}

	OrganizationRepo.set = function(data) {
		self.unwrap(self, data);
	};

	OrganizationRepo.get = function() {

		if(OrganizationRepo.promise) return OrganizationRepo.data;

		var newAllOrganizationsPromise = WsApi.fetch({
				endpoint: '/private/queue', 
				controller: 'organization', 
				method: 'all',
		});

		OrganizationRepo.promise = newAllOrganizationsPromise;

		if(OrganizationRepo.data) {
			newAllOrganizationsPromise.then(function(data) {
				OrganizationRepo.set(JSON.parse(data.body).payload.HashMap);
			});
		}
		else {
			OrganizationRepo.data = new OrganizationRepo(newAllOrganizationsPromise);	
		}
		
		OrganizationRepo.listener = WsApi.listen({
			endpoint: '/channel', 
			controller: 'organization', 
			method: '',
		});

		 WsApi.listen({
			endpoint: '/channel', 
			controller: 'organization', 
			method: 'workflow-step',
		}).then(null, null, function(res){
                  // console.info(res);
                  
                });

		OrganizationRepo.set(OrganizationRepo.listener);

		return OrganizationRepo.data;
	
	};

	OrganizationRepo.getChildren = function(id) {

		var childOrganizationsPromise = WsApi.fetch({
				endpoint: '/private/queue', 
				controller: 'organization', 
				method: 'get-children/' + id,
		});

		return childOrganizationsPromise;
	
	};

	OrganizationRepo.add = function() {

		var addOrganizationPromise = WsApi.fetch({
				'endpoint': '/private/queue', 
				'controller': 'organization', 
				'method': 'create',
				'data': {
					"name": OrganizationRepo.newOrganization.name, 
					"category": OrganizationRepo.newOrganization.category,
					"parentOrganizationId": OrganizationRepo.newOrganization.parent.id,
				}
		});

		OrganizationRepo.resetNewOrganization();

		return addOrganizationPromise;

	};

	OrganizationRepo.update = function(organization) {

		var updateOrganizationPromise = WsApi.fetch({
				'endpoint': '/private/queue', 
				'controller': 'organization', 
				'method': 'update',
				'data': {
					"organization": organization
				}
		});

		return updateOrganizationPromise;

	};

	OrganizationRepo.getStepForId = function(wsID) {

		var defer = $q.defer();

		if (workflowStepsCache[wsID]) {
			defer.resolve(workflowStepsCache[wsID]);
		}else{
			var stepPromise = WsApi.fetch({
				endpoint: '/private/queue', 
				controller: 'organization', 
				method: 'workflow-step/' + wsID,
			});

			stepPromise.then(function(result){
				var workflowStep = JSON.parse(result.body).payload.WorkflowStep;
				workflowStepsCache[wsID] = workflowStep;
				defer.resolve(workflowStep);
			});

		}
		return defer.promise;
	}
        
        OrganizationRepo.ready = function() {
                return OrganizationRepo.promise;
	};

	OrganizationRepo.listen = function() {
		return OrganizationRepo.listener;
	};
	
	return OrganizationRepo;
	
});
