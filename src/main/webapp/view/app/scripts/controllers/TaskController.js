"use strict";

eventManagerApp.controller("TaskCtrl",
    function ($scope, $state, task) {
    	$scope.task = task;
    });