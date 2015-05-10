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

        $scope.clear = function () {
            $scope.form = {
                mail: null,
                password: null
            };
        };

        $scope.clear();

        $scope.login = function () {
            $scope.message = AuthService.login($scope.form);
            console.log(AuthService.logged());
            $scope.clear();
        }

        $scope.register = function () {
            $scope.message = AuthService.register($scope.form);
            console.log(AuthService.logged());
            $scope.clear();
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
        var message = 'вообще, это сообщение должно сигнализировать о том, что логин или пароль неверный. (если будет написано на этом месте Errror) можешь глянуть в консоль';
        $http.post("/auth", form).then(
                function (data, status, headers, config) {
                    logged = true;
                    console.log("logged=" + logged);
                    message = "Должно быть все ок. Вы не должны этого видеть";

                },
                function (data, status, headers, config) {
                    console.log("Error logged=" + logged);
                    message = "Errror";
                });
        return message;
    };

    var logout = function () {
        logged = false;
        console.log("logout");
    };

    var register = function (form) {
        var message = "register";
        
        $http.post("/registration", form).then(
                function (data, status, headers, config) {
                    console.log(data);
                    message = "";
                },
                function (data, status, headers, config) {
                    message = "Some trouble, я чувствую. Возможно, пользователь с таким мылом уже есть";
                });

        return message;
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