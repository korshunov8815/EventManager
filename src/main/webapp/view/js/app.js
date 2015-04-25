"use strict";


console.log("hello");


var eventManagerApp = angular.module("EventManagerApp", ["ngResource"]);

eventManagerApp.factory("Event", ["$resource",
    function ($resource) {
        return $resource("/events/:id", {id: "@id"}, {});
    }]);

eventManagerApp.controller("MainPageCtrl", ["$scope", "Event", "$http",
    function ($scope, Event, $http) {

        $scope.mother = $http.get("/events");
        $scope.form = new Event();

        $scope.mother.success(function(data, status, headers, config) {
            console.log("json please");
            console.log(data);
        });

        $scope.events = Event.query();
        $scope.events.$promise.then(
            function (data, status, headers, config) {
                console.log($scope.events);
            },
            function () {
                console.log("f*ck");
            }
        );

        $scope.submit = function(form) {
            console.log($scope.form);
            $scope.form.$save();
        };
    }]);