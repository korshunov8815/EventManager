"use strict";

eventManagerApp.controller("EventCtrl",
    function ($scope, $state, Task, event) {
    	$scope.event = event;
    	$scope.form = {}
    	$scope.editing = {
            event: false,
            task: false
        };

    	$scope.toggle_editing = function (val) {
			$scope.editing[val] = !$scope.editing[val];
    	}

    	$scope.deleteEvent = function () {
    		$scope.event.$delete().then(
    			function () {
    				$state.go("events");
    			});

    	};

    	$scope.editEvent = function () {
    		$scope.form.title = $scope.event.title;
    		$scope.form.description = $scope.event.description;

    		$scope.toggle_editing("event");
    	};

    	$scope.cancelEditEvent = function () {
    		$scope.toggle_editing("event");
    	};

    	$scope.saveEvent = function () {
    		$scope.event.title = $scope.form.title;
    		$scope.event.description = $scope.form.description;

    		$scope.event.$save();

    		$scope.toggle_editing();
    	};

        $scope.addTask= function () {
            $scope.task = new Task();

            $scope.toggle_editing("task");

        };

        $scope.saveTask = function () {
            console.log("Сохраняю таск");
            $scope.task.$save().then(
                function () {
                    console.log("SUPER GOOD. TASK СОХРАНЕН");
                }, function () {
                    console.log("Все очень плохо");
                });
            $scope.toggle_editing("task");
        };

        $scope.cancelEditTask = function () {
            $scope.toggle_editing("task");
        };
    });