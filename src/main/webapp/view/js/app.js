"use strict";


console.log("hello");


var eventManagerApp = angular.module("EventManagerApp", ["ngResource"]);

eventManagerApp.factory("Event", ["$resource",
    function ($resource) {
        return $resource("/events/:id", {id: "@id"}, {});
    }]);

eventManagerApp.controller("MainPageCtrl", ["$scope", "Event",
    function ($scope, Event) {
        $scope.events = Event.query();
        $scope.events.$promise.then(
            function (data, status, headers, config) {
                console.log("oyyyy");
            },
            function () {
                console.log("f*ck");
            }
        );
        $scope.wow = "hello";
    }]);