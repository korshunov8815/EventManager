"use strict";

eventManagerApp.controller("EventCtrl",
    function ($scope, $state, Task, Participant, AuthService, event) {
    	$scope.event = event;
    	$scope.tasks = Task.getTasksByEventId({eventId: $scope.event.id});
        $scope.participants = Participant.getParticipantsByEventId({eventId: $scope.event.id});
        $scope.form = {}
    	$scope.editing = {
            event: false,
            task: false
        };

    	$scope.toggle_editing = function (val) {
            console.log($scope.editing[val]);
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
            console.log("saveEvent");
    		$scope.event.title = $scope.form.title;
    		$scope.event.description = $scope.form.description;

    		$scope.event.$save();

    		$scope.toggle_editing("event");
    	};

        $scope.takeTask = function (task) {
            var task = new Task(task);
            task.$patch().then(
                function () {
                    $scope.tasks = Task.getTasksByEventId({eventId: $scope.event.id});
                }, function () {
                    console.log("Все очень плохо");
                });;
        }

        $scope.editTask = function (task) {
            $scope.task = new Task(task);
            
            $scope.toggle_editing("task");            
        }

        $scope.addTask= function () {
            $scope.editTask(new Task({taskEventKeeper: {id: $scope.event.id}}));
        };

        $scope.saveTask = function () {
            console.log("Сохраняю таск");
            $scope.task.$save().then(
                function () {
                    $scope.tasks = Task.getTasksByEventId({eventId: $scope.event.id});
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
                    $scope.tasks = Task.getTasksByEventId({eventId: $scope.event.id});
                }, function () {
                    console.log("Bad deletion");
                })
        };

        $scope.confirmTask = function (task) {
            var to_confirm = new Task({id: task.id, isDone: true});
            to_confirm.$put().then(
                function () {
                    $scope.tasks = Task.getTasksByEventId({eventId: $scope.event.id});
                }, function () {
                    console.log("Bad confirm");
                });
        };

        $scope.strike_class = function (task) {
            return task.isDone?"strike":"";
        };

        $scope.isCreator = function () {
            return AuthService.user && $scope.event.eventCreator.id == AuthService.user.id;
        };

        $scope.isParticipant = function() {
            console.log($scope.participants.length);
            return $scope.participants.reduce(function (a, b) {
                return a || (AuthService.user && b.id == AuthService.user.id);
            }, false);
        };

        $scope.takePart = function () {
            $scope.event.$patch().then(
                function () {
                    $scope.tasks = Task.getTasksByEventId({eventId: $scope.event.id});
                    $scope.participants = Participant.getParticipantsByEventId({eventId: $scope.event.id});
                }, function () {
                    console.log("bad");
                });
        };

        $scope.leaveEvent = function () {
            $scope.event.$leave().then(
                function () {
                    $scope.tasks = Task.getTasksByEventId({eventId: $scope.event.id});
                    $scope.participants = Participant.getParticipantsByEventId({eventId: $scope.event.id});
                }, function () {
                    console.log("bad");
                });
        }
    });