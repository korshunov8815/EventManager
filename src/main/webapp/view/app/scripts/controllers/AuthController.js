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

        $scope.register = function (credentials) {
            AuthService.register(credentials).then(function (user) {
                console.log("good:" + user);
            }, function () {
                console.log("bad");
            })
        };
    });