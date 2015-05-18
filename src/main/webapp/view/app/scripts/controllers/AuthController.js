"use strict";

eventManagerApp.controller("AuthController",
    function ($scope, $rootScope, AuthService) {

        $scope.AuthService = AuthService;

        $scope.credentials = {
            email: '',
            password: ''
        };

        // $scope.clear();

        $scope.login = function (credentials) {
            AuthService.login(credentials).then(function (user) {
                // $rootScope.$broadcast(AUTH_EVENTS.loginSuccess);
                // $scope.setCurrentUser(user);
                console.log(user);
            });
            // $scope.message = AuthService.login($scope.form);
            // console.log(AuthService.logged());
            // $scope.form.password = null;
        };

        $scope.register = function (credentials) {
            AuthService.register(credentials).then(function (user) {
                console.log("good:" + user);
            }, function () {
                console.log("bad");
            })
        };

        // $scope.register = function () {
        //     $scope.message = AuthService.register($scope.form);
        //     console.log(AuthService.logged());
        //     $scope.form.password = null;
        // }

        // $scope.logout = function () {
        //     AuthService.logout();
        // }

        // $scope.logged = function () {
        //     return AuthService.logged();
        // }
    });