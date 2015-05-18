"use strict";

eventManagerApp.controller("EventsCtrl",
    function ($scope, $state, Event, events) {
    	$scope.show_events = true;

    	$scope.event = new Event();

    	$scope.events = events;

    	$scope.toggle_show_events = function () {
    		$scope.show_events = !$scope.show_events;
    	};

    	$scope.createEvent = function () {
    		$scope.event.$save().then(function () {
    			$scope.events = Event.query();
    			$scope.toggle_show_events();
    			$scope.event = new Event();
    		});
    	}

    	$scope.addEvent = function () {
    		$scope.toggle_show_events();
    	}
    });