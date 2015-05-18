"use strict";

eventManagerApp.controller("EventsCtrl",
    function ($scope, Event, Events) {
    	$scope.show_events = true;

    	$scope.event = new Event();

    	$scope.Events = Events;

    	$scope.toggle_show_events = function () {
    		$scope.show_events = !$scope.show_events;
    	};

    	$scope.createEvent = function () {
    		$scope.event.$save();
    	}

    	$scope.addEvent = function () {
    		$scope.toggle_show_events();
    	}
    });