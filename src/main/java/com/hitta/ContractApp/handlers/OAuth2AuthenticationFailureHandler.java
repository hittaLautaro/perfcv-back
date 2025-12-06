package com.hitta.ContractApp.handlers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class OAuth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Value("${FRONTEND_URL}")
    private String frontendUrl;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {

        String errorMessage = exception.getLocalizedMessage();
        String encodedError = URLEncoder.encode(errorMessage, StandardCharsets.UTF_8);

        String redirectUrl = frontendUrl + "/login?error=oauth2_failed&message=" + encodedError;

        logger.error("OAuth2 authentication failed: " + errorMessage, exception);

        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}
