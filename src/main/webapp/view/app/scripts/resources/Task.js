"use strict";

eventManagerApp.factory("Task", ["$resource",
    function ($resource) {
        return $resource("/api/tasks/:id", {id: "@id"},
        	{put: {method: "PUT"},
        	 patch: {method: "PATCH"}});
    }]);