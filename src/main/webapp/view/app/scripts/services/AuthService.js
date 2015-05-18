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
                    $state.go("events");
                });
        };

        authService.getUser = function () {
            return $http
                .get("/api/auth")
                .then(function (res) {
                    authService.user = res.data;
                }, function (res) {
                    console.log("getUser fail")
                    $rootScope.$broadcast(AUTH_EVENTS.loginFailed);
                })
        }

        authService.isLogged = function () {
            return !!authService.user;
        };

        authService.logout = function () {
            authService.user = null;
            console.log(ipCookie("sessionId"));
            $rootScope.$broadcast(AUTH_EVENTS.loginFailed);
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