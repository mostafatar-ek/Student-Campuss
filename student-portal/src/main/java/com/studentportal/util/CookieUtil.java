package com.studentportal.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CookieUtil {

    private static final String COOKIE_NAME = "studentId";
    private static final int MAX_AGE = 7 * 24 * 60 * 60;

    public static void set(HttpServletResponse response, Long studentId) {
        Cookie cookie = new Cookie(COOKIE_NAME, String.valueOf(studentId));
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(MAX_AGE);
        response.addCookie(cookie);
    }

    public static Long get(HttpServletRequest request) {
        if (request.getCookies() == null) return null;
        for (Cookie cookie : request.getCookies()) {
            if (COOKIE_NAME.equals(cookie.getName())) {
                try { return Long.parseLong(cookie.getValue()); }
                catch (NumberFormatException e) { return null; }
            }
        }
        return null;
    }

    public static void clear(HttpServletResponse response) {
        Cookie cookie = new Cookie(COOKIE_NAME, "");
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
