"use strict";



var eventManagerApp = angular.module("EventManagerApp", ["ngResource", "ngCookies", "ui.router"])
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