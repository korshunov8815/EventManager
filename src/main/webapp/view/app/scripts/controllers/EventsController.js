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
    	};

    	$scope.addEvent = function () {
    		$scope.toggle_show_events();
    	};

        // $scope.event_filter = function () {
        //     var today = new Date();
        //     today.setHours(0);
        //     today.setMinutes(0);
        //     today.setSeconds(0);
        //     today.setMilliseconds(0);
        //     return function (event) {
        //         var date = new Date(event.datetime);
        //         date.setHours(0);
        //         date.setMinutes(0);
        //         date.setSeconds(0);
        //         date.setMilliseconds(0);
        //         console.log(date, today, date >= today);
        //         return date >= today;
        //     };
        // };

    });