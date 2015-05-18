"use strict";

eventManagerApp.controller("EventsCtrl",
    function ($scope, $state, Event, Events) {
    	$scope.show_events = true;

    	$scope.event = new Event();

    	$scope.Events = Events;

    	$scope.toggle_show_events = function () {
    		$scope.show_events = !$scope.show_events;
    	};

    	$scope.createEvent = function () {
    		$scope.event.$save().then(function () {
    			$scope.Events = Event.query();
    			$scope.toggle_show_events();
    			console.log($scope.Events);
    		});
    	}

    	$scope.addEvent = function () {
    		$scope.toggle_show_events();
    	}
    });