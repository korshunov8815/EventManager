"use strict";

eventManagerApp.controller("EventCtrl",
    function ($scope, $state, Task, event) {
    	$scope.event = event;
    	$scope.form = {}
    	$scope.editing = {
            event: false,
            task: false
        };
        $scope.tasks = [
            {
                id: 1,
                "participant": {
                    id: 2,
                    "name": "ganshinv@gmail.com"
                },
                "description": "Just do it"
            },
            {
                id: 2,
                "participant": {
                    id: 2,
                    "name": "ganshinv@gmail.com"
                },
                "description": "Just do it"
            }
        ];

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

        $scope.editTask = function (task) {
            $scope.task = new Task(task);
            
            $scope.toggle_editing("task");            
        }

        $scope.addTask= function () {
            $scope.editTask(new Task({event: $scope.event.id}));
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

        $scope.deleteTask = function (task) {
            var to_delete = new Task(task);
            to_delete.$delete().then(
                function () {
                    console.log("Good deletion");
                }, function () {
                    console.log("Bad deletion");
                })
        };

        $scope.confirmTask = function (task) {
            console.log(task);
            console.log({id: task.id, confirm: true});
            var to_confirm = new Task({id: task.id, confirm: true});
            to_confirm.$patch().then(
                function () {
                    console.log("Good confirm");
                }, function () {
                    console.log("Bad confirm");
                });
        }
    });