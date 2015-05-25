"use strict";

eventManagerApp.controller("ParticipantCtrl",
    function ($scope, $state, user, Task, Event, AuthService) {
    	$scope.user = user;
        $scope.events = Event.getEventsByParticipantId({userId: $scope.user.id});
        $scope.tasks = Task.getTasksByParticipantId({userId: $scope.user.id});
        $scope.avatar = "http://gravatar.com/avatar/" + hex_md5(user.email) + "?s=50&d=retro";

    	$scope.save_changes = function () {
    		$scope.user.$put()
    			.then(function () {
    				AuthService.getUser();
    				$state.go("events");
    			});
    	};

        $scope.strike_class = function (task) {
            return task.isDone?"strike":"";
        };

        $scope.confirmTask = function (task) {
            var to_confirm = new Task({id: task.id, isDone: true});
            to_confirm.$put().then(
                function () {
                    $scope.tasks = Task.getTasksByParticipantId({userId: $scope.user.id});
                }, function () {
                    console.log("Bad confirm");
                });
        };
    });