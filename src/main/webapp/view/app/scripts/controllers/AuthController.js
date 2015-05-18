"use strict";

eventManagerApp.controller("AuthController",
    function ($scope, $rootScope, AuthService) {

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
    });