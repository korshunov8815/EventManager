"use strict";

eventManagerApp.factory("Profile", ["$resource",
    function ($resource) {
        return $resource("/api/auth", {},
        	{put: {method: "PUT"}});
    }]);