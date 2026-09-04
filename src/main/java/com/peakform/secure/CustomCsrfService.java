package com.peakform.secure;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
public class CustomCsrfService {

    private static final String SESSION_ATTR = "peakform_csrf_token";
    private final SecureRandom secureRandom = new SecureRandom();

    public String getOrCreateToken(HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        String token = (String) session.getAttribute(SESSION_ATTR);
        if (token == null) {
            token = generateToken();
            session.setAttribute(SESSION_ATTR, token);
        }
        return token;
    }

    private String generateToken() {
        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    public boolean isTokenValid(HttpServletRequest request, String submittedToken) {
        HttpSession session = request.getSession(false);
        if (session == null || submittedToken == null) return false;
        String expected = (String) session.getAttribute(SESSION_ATTR);
        return expected != null && expected.equals(submittedToken);
    }

    public boolean isRefererValid(HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        if (referer == null) return false;
        String host = request.getServerName();
        return referer.contains("://" + host);
    }
}
