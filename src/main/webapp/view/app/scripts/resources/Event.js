"use strict";

eventManagerApp.factory("Event", ["$resource",
    function ($resource) {
        return $resource("/api/events/:id", {id: "@id"},
        	{put: {method: "PUT"},
        	 patch: {method: "PATCH"}});
    }]);