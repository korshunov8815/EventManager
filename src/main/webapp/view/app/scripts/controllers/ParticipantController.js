"use strict";

eventManagerApp.controller("ParticipantCtrl",
    function ($scope, $state, $http, user, Participant, Task, Event, AuthService) {
    	$scope.user = user;
        $scope.events = Event.getEventsByParticipantId({userId: $scope.user.id});
        $scope.tasks = Task.getTasksByParticipantId({userId: $scope.user.id});
        $scope.avatar = "http://gravatar.com/avatar/" + hex_md5(user.email) + "?s=50&d=retro";

        $scope.my_profile = function () {
            return AuthService.user && AuthService.user.id == $scope.user.id;
        };

        $scope.status = {
            editing: false
        };

        $scope.form = {
            name: $scope.user.name
        };

        $scope.edit = function () {
            $scope.form.name = $scope.user.name;
            $scope.status.editing = true;
        }

        $scope.cancelEdit = function () {
            $scope.status.editing = false;
        }

    	$scope.save_changes = function () {
            $scope.user.name = $scope.form.name;
    		$http.put("/api/auth", $scope.user)
    			.success(function () {
    				AuthService.getUser().then(
                        function () {
                            $scope.user = AuthService.user;
                        });
    			});
            $scope.status.editing = false;
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