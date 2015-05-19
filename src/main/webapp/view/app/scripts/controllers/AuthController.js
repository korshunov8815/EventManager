"use strict";

eventManagerApp.controller("AuthController",
    function ($scope, $rootScope, AuthService, AUTH_EVENTS) {

        $scope.avatar = "http://gravatar.com/avatar/?s=50&d=retro";

        $scope.AuthService = AuthService;

        $scope.credentials = {
            email: '',
            password: ''
        };

        $scope.login = function (credentials) {
            AuthService.login(credentials);
        };

        $scope.logout = function () {
            AuthService.logout();
        };

        $scope.register = function (credentials) {
            AuthService.register(credentials).then(function (user) {
                console.log("good:" + user);
            }, function () {
                console.log("bad");
            })
        };

        $rootScope.$on(AUTH_EVENTS.loginSuccess, function () {
            console.log("yes i'm here")
            $scope.avatar = "http://gravatar.com/avatar/" + hex_md5(AuthService.user.email) + "?s=50&d=retro";
        });
    });