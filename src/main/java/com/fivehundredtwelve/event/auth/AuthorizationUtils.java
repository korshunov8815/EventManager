package com.fivehundredtwelve.event.auth;

import com.fivehundredtwelve.event.model.Participant;
import com.fivehundredtwelve.event.service.SessionService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by anna on 27.04.15.
 */
public class AuthorizationUtils {

    public static String encodeMD5(String value) {
        MD5 md5 = new MD5();
        return md5.getHash(value);
    }

    public static Participant authorize(final HttpServletRequest request, final SessionService sessionService) {
        try {
            Cookie[] cookie = request.getCookies();
            String currentSessionId = "";
            for (Cookie c : cookie) {
                if (c.getName().equalsIgnoreCase("sessionID")) currentSessionId = c.getValue();
            }
            if (sessionService.ifSessionExist(currentSessionId)) {
                return sessionService.getParticipantBySession(currentSessionId);
            }
            else throw new Exception("no session");
        }
        catch (final Exception e) {
            return null;
        }
    }
}
