"use strict";

eventManagerApp.factory("Participant", ["$resource",
    function ($resource) {
        return $resource("/api/participants/:id", {id: "@id"},
        	{put: {method: "PUT"},
        	 patch: {method: "PATCH"},
        	 getParticipantsByEventId: {method: "GET", url: "/api/events/:eventId/participants", isArray: true}});
    }]);