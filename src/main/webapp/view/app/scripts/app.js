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
                url: "/",
                templateUrl: "/app/views/index.html",
                controller: "IndexCtrl"
            })
            .state("events", {
                url: "/events",
                templateUrl: "/app/views/events.html",
                controller: "EventsCtrl",
                resolve: {
                    Events: function (Event) {
                        return Event.query().$promise;
                    }
                }
            })
            .state("index.events.instance", {
                url: "^/:id",
                templateUrl: "/app/views/event.html",
                controller: "EventCtrl"
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
        $rootScope.$on(AUTH_EVENTS.loginSuccess, function () {
            $state.go("events");
        });
        
        $rootScope.$on(AUTH_EVENTS.loginFailed, function () {
            $state.go("index");
        });
    });
