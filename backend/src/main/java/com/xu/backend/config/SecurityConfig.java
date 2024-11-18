package com.xu.backend.config;

import com.xu.backend.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.*;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.function.Supplier;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserService userService;

    /**
     * Used by the FilterChainProxy to determine which Spring Security Filter instances should
     * be invoked for the current process. This filter chain will be configured for 2FA.
     *
     * @param http Configures the web security for specific HTTP requests
     * @return A SecurityFilterChain configuration
     * @throws Exception If an error occurs
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .csrfTokenRequestHandler(new SpaCsrfTokenRequestHandler())
                )
                .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
                .authorizeHttpRequests((requests) -> requests
                                .requestMatchers("/csrf").permitAll()
                                .anyRequest().access(new MfaAuthorizationManager(userService))
                        //.requestMatchers("/**").authenticated()
                ).anonymous(AbstractHttpConfigurer::disable)
                .oauth2Login((oauth2) -> oauth2
                        .defaultSuccessUrl("http://localhost:5173/oauth2-success")
                        .failureUrl("http://localhost:5173/oauth2-fail")
                )
                //.addFilterAfter(new MfaFilter(userService), BasicAuthenticationFilter.class)
                .logout(logout -> logout
                        .deleteCookies("JSESSIONID")
                        .logoutSuccessHandler((request, response, authentication) -> {
                            response.setStatus(HttpStatus.OK.value());
                            response.setContentType("text/plain");
                            response.getWriter().println("Logout successful");
                        })
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(unauthorizedEntryPoint())
                )
        ;
        return http.build();
    }

    /**
     * Creates an AuthenticationEntryPoint object that returns an unauthorized status code.
     *
     * @return An AuthenticationEntryPoint object
     */
    @Bean
    public AuthenticationEntryPoint unauthorizedEntryPoint() {
        return new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED);
    }
}

/**
 * A filter that renders the CSRF token value to a cookie.
 * REFERENCE: <a href="https://docs.spring.io/spring-security/reference/servlet/exploits/csrf.html#csrf-integration-javascript-spa">...</a>
 */
final class CsrfCookieFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        CsrfToken csrfToken = (CsrfToken) request.getAttribute("_csrf");
        // Render the token value to a cookie by causing the deferred token to be loaded
        csrfToken.getToken();

        filterChain.doFilter(request, response);
    }
}

/**
 * A custom implementation of CsrfTokenRequestHandler that uses the XorCsrfTokenRequestAttributeHandler
 * to provide BREACH protection of the CsrfToken when it is rendered in the response body.
 * REFERENCE: <a href="https://docs.spring.io/spring-security/reference/servlet/exploits/csrf.html#csrf-integration-javascript-spa">...</a>
 */
final class SpaCsrfTokenRequestHandler extends CsrfTokenRequestAttributeHandler {
    private final CsrfTokenRequestHandler delegate = new XorCsrfTokenRequestAttributeHandler();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, Supplier<CsrfToken> csrfToken) {
        /*
         * Always use XorCsrfTokenRequestAttributeHandler to provide BREACH protection of
         * the CsrfToken when it is rendered in the response body.
         */
        this.delegate.handle(request, response, csrfToken);
    }

    @Override
    public String resolveCsrfTokenValue(HttpServletRequest request, CsrfToken csrfToken) {
        /*
         * If the request contains a request header, use CsrfTokenRequestAttributeHandler
         * to resolve the CsrfToken. This applies when a single-page application includes
         * the header value automatically, which was obtained via a cookie containing the
         * raw CsrfToken.
         */
        if (StringUtils.hasText(request.getHeader(csrfToken.getHeaderName()))) {
            return super.resolveCsrfTokenValue(request, csrfToken);
        }
        /*
         * In all other cases (e.g. if the request contains a request parameter), use
         * XorCsrfTokenRequestAttributeHandler to resolve the CsrfToken. This applies
         * when a server-side rendered form includes the _csrf request parameter as a
         * hidden input.
         */
        return this.delegate.resolveCsrfTokenValue(request, csrfToken);
    }
}

/**
 * A custom MfaAuthorizationManager that checks if the user has been authenticated with MFA.
 * If the user has not been authenticated with MFA, the request is blocked. This filter
 * is bypassed if the user does not have MFA enabled. A valid session is set with the
 * mfaAuthenticated attribute when the user has been authenticated with MFA.
 */
@RequiredArgsConstructor
@Component
final class MfaAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    private final UserService userService;

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        Authentication auth = authentication.get();

        // Deny if not authenticated
        if (auth == null || !auth.isAuthenticated()) {
            return new AuthorizationDecision(false);
        }

        // Bypass this filter if the user does not have MFA enabled
        if (!userService.findTwoFactorEnabledById(auth.getName())) {
            return new AuthorizationDecision(true);
        }

        // Enforce MFA
        // Check if the user has been authenticated with MFA (valid session set with mfaAuthenticated attribute)
        HttpSession session = object.getRequest().getSession(false);
        boolean mfaAuthenticated = (session != null) && (Boolean.TRUE.equals(session.getAttribute("isTwoFactorAuthenticated")));

        // If the user has not been authenticated with MFA, block the request
        return mfaAuthenticated ? new AuthorizationDecision(true) : new AuthorizationDecision(false);
    }
}

