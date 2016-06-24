"use strict";

eventManagerApp.controller("TaskCtrl",
    function ($scope, $state, tasks) {
    	$scope.tasks = tasks;
    });