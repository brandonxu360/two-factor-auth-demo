package com.xu.backend.filter;

import com.xu.backend.service.UserService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

/**
 * Filter that checks if the user has been authenticated with MFA.
 * If the user does not have MFA enabled, the request is allowed.
 * If the user has not been authenticated with MFA, the request is blocked.
 * A request is considered authenticated with MFA if the session attribute "mfaAuthenticated" is set to true.
 * NOTE: This filter is not being used - a custom AuthorizationManager is being used instead.
 */
@RequiredArgsConstructor
public class MfaFilter implements Filter {
    private final UserService userService;
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // Bypass this filter if the user does not have MFA enabled
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !userService.findTwoFactorEnabledById(authentication.getName())) {
            chain.doFilter(request, response);
            return;
        }

        // Check if the user has been authenticated with MFA
        HttpSession session = ((HttpServletRequest) request).getSession(false);
        boolean mfaAuthenticated = (session != null) && (Boolean.TRUE.equals(session.getAttribute("mfaAuthenticated")));

        // If the user has not been authenticated with MFA, block the request
        if (!mfaAuthenticated) {
            ((HttpServletResponse) response).sendError(401, "MFA authentication required");
            return;
        }

        // Continue the filter chain if the user has been authenticated with MFA
        chain.doFilter(request, response);
    }
}
