"use strict";

eventManagerApp.factory("AuthService",
    function($rootScope, $http, $state, AUTH_EVENTS) {
        var authService = {};

        authService.user = null;

        authService.login = function (creadentials) {
            console.log(creadentials);
            return $http
                .post("/api/auth", creadentials)
                .then(function (res) {
                    authService.user = res.data;
                    $rootScope.$broadcast(AUTH_EVENTS.loginSuccess);
                    $state.go("events");
                });
        };

        authService.getUser = function () {
            return $http
                .get("/api/auth")
                .then(function (res) {
                    authService.user = res.data;
                    $rootScope.$broadcast(AUTH_EVENTS.loginSuccess);
                }, function (res) {
                    console.log("getUser fail")
                    $rootScope.$broadcast(AUTH_EVENTS.loginFailed);
                })
        }

        authService.isLogged = function () {
            return !!authService.user;
        };

        authService.logout = function () {
            return $http
                .delete("/api/auth")
                .error(function () {
                    authService.user = null;
                    $rootScope.$broadcast(AUTH_EVENTS.loginFailed);
                })
        };

        authService.register = function (credentials) {
            return $http.post("/api/registration", credentials).then(
                    function (data, status, headers, config) {
                        console.log(data);
                    },
                    function (data, status, headers, config) {
                    });
        };

        return authService;
    });