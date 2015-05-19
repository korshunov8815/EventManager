"use strict";

eventManagerApp.controller("ProfileCtrl",
    function ($scope, $state, user, AuthService) {
    	$scope.user = user;

    	$scope.save_changes = function () {
    		$scope.user.$put()
    			.then(function () {
    				AuthService.getUser();
    				$state.go("events");
    			});
    	};
    });