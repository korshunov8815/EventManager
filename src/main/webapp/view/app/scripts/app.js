"use strict";

var eventManagerApp = angular.module("EventManagerApp", ["ngResource", "ui.router"],
    function ($stateProvider, $urlRouterProvider) {
        // $locationProvider.html5Mode({
        //   enabled: true,
        //   requireBase: false
        // });

        // $locationProvider.hashPrefix('!');

        $stateProvider
            .state("events", {
                url: "/events",
                templateUrl: "app/views/events.html",
                controller: "EventCtrl"
            })
    });

eventManagerApp.constant('AUTH_EVENTS', {
  loginSuccess: 'auth-login-success',
  loginFailed: 'auth-login-failed',
  logoutSuccess: 'auth-logout-success',
  sessionTimeout: 'auth-session-timeout',
  notAuthenticated: 'auth-not-authenticated',
  notAuthorized: 'auth-not-authorized'
});

eventManagerApp.constant('USER_ROLES', {
  all: '*',
  admin: 'admin',
  editor: 'editor',
  guest: 'guest'
});

eventManagerApp.controller("ApplicationController", ["$scope", "Event", "$http",
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


eventManagerApp.controller("LoginController",
    function ($scope, $rootScope, AUTH_EVENTS, AuthService) {

        $scope.credentials = {
            email: '',
            password: ''
        };

        // $scope.clear();

        $scope.login = function (credentials) {
            AuthService.login(credentials).then(function (user) {
                $rootScope.$broadcast(AUTH_EVENTS.loginSuccess);
                $scope.setCurrentUser(user);
            }, function () {
                $rootScope.$broadcast(AUTH_EVENTS.loginFailed);
            });
            // $scope.message = AuthService.login($scope.form);
            // console.log(AuthService.logged());
            // $scope.form.password = null;
        }

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

eventManagerApp.controller('ApplicationController', function ($scope,
                                               USER_ROLES,
                                               AuthService) {
  $scope.currentUser = null;
  $scope.userRoles = USER_ROLES;
  $scope.isAuthorized = AuthService.isAuthorized;
 
  $scope.setCurrentUser = function (user) {
    $scope.currentUser = user;
  };
});

eventManagerApp.service('Session', function () {
    this.create = function (sessionID) {
        this.sessionID = sessionID;
    }
  // this.create = function (sessionId, userId, userRole) {
  //   this.id = sessionId;
  //   this.userId = userId;
  //   this.userRole = userRole;
  // };
  this.destroy = function () {
    this.sessionID = null;
    // this.id = null;
    // this.userId = null;
    // this.userRole = null;
  };
});