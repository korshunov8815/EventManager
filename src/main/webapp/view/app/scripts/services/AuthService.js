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
                // .post("/api/auth", {email: "q@q", password: "qw"})
                .then(function (res) {
                    authService.user = res.data;
                    $rootScope.$broadcast(AUTH_EVENTS.loginSuccess);
                }, function (res) {
                    console.log("getUser fail")
                    $rootScope.$broadcast(AUTH_EVENTS.loginFailed)
                })
        }

        authService.isLogged = function () {
            return !!authService.user;
        };

        authService.logout = function () {
            authService.user = null;
            location = "/";
        };

        authService.register = function (credentials) {
            return $http.post("/api/registration", credentials).then(
                    function (data, status, headers, config) {
                        console.log(data);
                        // message = "";
                    },
                    function (data, status, headers, config) {
                        // message = "Some trouble, я чувствую. Возможно, пользователь с таким мылом уже есть";
                    });
        };

        return authService;
    });