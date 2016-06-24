"use strict";



var eventManagerApp = angular.module("EventManagerApp", ["ngResource", "ui.router"])
    .config(function ($httpProvider, $stateProvider, $urlRouterProvider, $locationProvider) {
        // $locationProvider.html5Mode({
        //   enabled: true,
        //   requireBase: false
        // });
        // $locationProvider.hashPrefix('!');

        $httpProvider.interceptors.push([
          '$injector',
          function ($injector) {
            return $injector.get('AuthInterceptor');
          }
        ]);

        $stateProvider
            .state("index", {
                url: "",
                templateUrl: "/app/views/index.html",
                controller: "IndexCtrl"
            })
            .state("profile", {
                url: "/profile",
                templateUrl: "/app/views/profile.html",
                controller: "ProfileCtrl",
                resolve: {
                    user: function (Profile) {
                        return Profile.get().$promise;
                    }
                }
            })
            .state("events", {
                url: "/events",
                templateUrl: "/app/views/events.html",
                controller: "EventsCtrl",
                resolve: {
                    events: function (Event) {
                        return Event.query().$promise;
                    }
                }
            })
            .state("event", {
                url: "/events/:id",
                templateUrl: "/app/views/event.html",
                controller: "EventCtrl",
                resolve: {
                    event: function (Event, $stateParams) {
                        return Event.get({id: $stateParams.id}).$promise;
                    }
                }
            })
            .state("participant", {
                url: "/participants/:id",
                templateUrl: "/app/views/participant.html",
                controller: "ParticipantCtrl",
                resolve: {
                    user: function (Participant, $stateParams) {
                        return Participant.get({id: $stateParams.id}).$promise;
                    }
                }
            })
    })

    .constant('AUTH_EVENTS', {
        loginSuccess: 'auth-login-success',
        loginFailed: 'auth-login-failed',
        logoutSuccess: 'auth-logout-success',
        sessionTimeout: 'auth-session-timeout',
        notAuthenticated: 'auth-not-authenticated',
        notAuthorized: 'auth-not-authorized'
    })

    .run(function (AuthService) {
        AuthService.getUser();
    })

    .factory('AuthInterceptor', function ($q, $rootScope, AUTH_EVENTS) {
      return {
        responseError: function (response) {
          if (response.status == 401) {
            $rootScope.$broadcast(AUTH_EVENTS.loginFailed);
          }
          return $q.reject(response);
        }
      };
    })

    .controller("AppCtrl", function ($rootScope, $state, AUTH_EVENTS) {
        // $rootScope.$on(AUTH_EVENTS.loginSuccess, function () {
        //     $state.go("events");
        // });
        
        $rootScope.$on(AUTH_EVENTS.loginFailed, function () {
            $state.go("index");
        });

        $rootScope.is_past_event = function (event) {
            var today = new Date();
            var date = new Date(event.datetime);
            today.setHours(0);
            date.setHours(0);
            today.setSeconds(0);
            date.setSeconds(0);
            today.setMilliseconds(0);
            date.setMinutes(0);
            today.setMinutes(0);
            date.setMilliseconds(0);
            console.log(event, date >= today);
            return date>=today?"past_event":"";
        };
    });

eventManagerApp.controller("Wow", function($scope, $http) {
        $scope.form = {
            file: null,
            date: null
        };
        $scope.message = "wow";
        $scope.send = function () {
            console.log($scope.form);
            $scope.message = "пжжжжжж....";
            $http.post("/api/antoshka_zalivaet_fotki", $scope.form).then(
                function () {
                    $scope.message = "Supper good";
                }, function () {
                    $scope.message = "It's a big problem";
                });
        }
    }); 