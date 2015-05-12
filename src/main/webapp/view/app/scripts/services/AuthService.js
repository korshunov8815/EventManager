"use strict";

eventManagerApp.factory("AuthService",
    function($http, Session) {
        var authService = {};

        authService.login = function (creadentials) {
            console.log(creadentials);
            return $http
                .post("/api/auth", creadentials)
                .then(function (res) {
                    Session.create(res.data.sessionID);
                    return res.data.sessionID;
                });
        };

        authService.isAuthenticated = function () {
            return !!Session.userId;
        };

        authService.isAuthorized = function (authorizedRoles) {
            if (!angular.isArray(authorizedRoles)) {
                authorizedRoles = [authorizedRoles];
            }

            return (authService.isAuthenticated() && authorizedRoles.indexOf(Session.userRole) !== -1);
        };

        return authService;


        // var logged = false;

        // var login = function (credentials) {
        //     var message = 'вообще, это сообщение должно сигнализировать о том, что логин или пароль неверный. (если будет написано на этом месте Errror) можешь глянуть в консоль';
        //     $http.post("/api/auth", credentials).then(
        //             function (data, status, headers, config) {
        //                 logged = true;
        //                 console.log("logged=" + logged);
        //                 message = "Должно быть все ок. Вы не должны этого видеть";

        //             },
        //             function (data, status, headers, config) {
        //                 console.log("Error logged=" + logged);
        //                 message = "Errror";
        //             });
        //     return message;
        // };

        // var logout = function () {
        //     logged = false;
        //     console.log("logout");
        // };

        // var register = function (credentials) {
        //     var message = "register";
            
        //     $http.post("/api/registration", credentials).then(
        //             function (data, status, headers, config) {
        //                 console.log(data);
        //                 message = "";
        //             },
        //             function (data, status, headers, config) {
        //                 message = "Some trouble, я чувствую. Возможно, пользователь с таким мылом уже есть";
        //             });

        //     return message;
        // };

        // var isLogged = function () {
        //     return logged;
        // };

        // return {
        //     login: login,
        //     logout: logout,
        //     register: register,
        //     logged: isLogged
        // }
    });