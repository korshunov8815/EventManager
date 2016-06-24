package com.fivehundredtwelve.event.auth;

import com.fivehundredtwelve.event.model.Participant;
import com.fivehundredtwelve.event.service.SessionService;

import javax.mail.MessagingException;
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

    public static void registration(String email, String id) throws MessagingException{
        String content = "dude check this link http://localhost:8181/api/confirm/"+id;
        Mailer.send(email,content);
    }



}
