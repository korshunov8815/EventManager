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

eventManagerApp.controller("AuthCtrl", ["$scope", "$http",
    function ($scope, $http) {
        $scope.form = {
            username: null,
            password: null
        };

        $scope.login = function () {
            $http.post("/auth", $scope.form).then(
                function (data, status, headers, config) {
                    console.log("Good job!");
                },
                function (data, status, headers, config) {
                    console.log("Some trouble, я думаю.");
                });
        }

    }]);