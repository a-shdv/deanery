package org.university.deanery.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;


import java.io.IOException;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
//        String username = request.getParameter("username");
        response.sendRedirect("sign-in?error=" + exception.getClass().getSimpleName());
    }

//    private static int failureCount = 0;
//    private AuthenticationException exception;
//
//    public AuthenticationException getException() {
//        return exception;
//    }
//
//    public void setException(AuthenticationException exception) {
//        this.exception = exception;
//    }
//
//    public static int getFailureCount() {
//        return failureCount;
//    }
//
//    @Override
//    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
//        failureCount++;
//        if (failureCount >= 3) {
//            System.exit(0);
//        }
//        this.exception = exception;
//        super.onAuthenticationFailure(request, response, exception);
//    }
}
