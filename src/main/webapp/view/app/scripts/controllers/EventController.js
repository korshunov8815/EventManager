"use strict";

eventManagerApp.controller("EventCtrl",
    function ($scope, $state, event) {
    	$scope.event = event;
    	$scope.form = {}
    	$scope.editing = false;

    	$scope.toggle_editing = function () {
			$scope.editing = !$scope.editing;
    	}

    	$scope.deleteEvent = function () {
    		$scope.event.$delete();

    		$state.go("events");
    	};

    	$scope.editEvent = function () {
    		$scope.form.title = $scope.event.title;
    		$scope.form.description = $scope.event.description;

    		$scope.toggle_editing();
    	};

    	$scope.cancelEditEvent = function () {
    		$scope.toggle_editing();
    	};

    	$scope.saveEvent = function () {
    		$scope.event.title = $scope.form.title;
    		$scope.event.description = $scope.form.description;

    		$scope.event.$save();

    		$scope.toggle_editing();
    	};
    });