"use strict";

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

eventManagerApp.controller("AuthCtrl", ["$scope", "$http", "AuthService",
    function ($scope, $http, AuthService) {
        $scope.form = {
            mail: null,
            password: null
        };

        $scope.login = function () {
            $scope.message = AuthService.login($scope.form);
            console.log(AuthService.logged());
            
        }

        $scope.register = function () {
            AuthService.register($scope.form);
            console.log(AuthService.logged());
            
        }

        $scope.logout = function () {
            AuthService.logout();
        }

        $scope.logged = function () {
            return AuthService.logged();
        }
    }]);

eventManagerApp.factory("AuthService", function($http) {
    var logged = false;


    var login = function (form) {        
        $http.post("/auth", form).then(
                function (data, status, headers, config) {
                    logged = true;
                    return "";
                },
                function (data, status, headers, config) {
                    return "Some trouble, я думаю.";
                });
    };

    var logout = function () {
        logged = false;
        console.log("logout");
    };

    var register = function (form) {
        console.log("register");
        $http.post("/registration", form).then(
                function (data, status, headers, config) {
                    console.log(data);
                    console.log("Good job!");
                },
                function (data, status, headers, config) {
                    console.log("Some trouble, я думаю.");
                });
    };

    var isLogged = function () {
        return logged;
    };

    return {
        login: login,
        logout: logout,
        register: register,
        logged: isLogged
    }
})