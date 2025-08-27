package be.ddd.infra.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

public class Auth0JwtFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(Auth0JwtFilter.class);
    private final Auth0JwtVerifier verifier;

    public Auth0JwtFilter(Auth0JwtVerifier verifier) {
        this.verifier = verifier;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {

        String auth = req.getHeader("Authorization");
        log.info("Auth0JwtFilter: Received Authorization header: {}", auth);

        if (!StringUtils.hasText(auth) || !auth.startsWith("Bearer ")) {
            log.warn("Auth0JwtFilter: No Bearer token found or invalid format.");
            chain.doFilter(req, res);
            return;
        }

        String token = auth.substring(7);
        log.info(
                "Auth0JwtFilter: Extracted JWT token (first 10 chars): {}...",
                token.substring(0, Math.min(token.length(), 10)));

        try {
            DecodedJWT jwt = verifier.verify(token);

            req.setAttribute("auth0.jwt", jwt);
            req.setAttribute("auth0.sub", jwt.getSubject());
            req.setAttribute("auth0.email", jwt.getClaim("email").asString());

            log.info(
                    "Auth0JwtFilter: JWT verified successfully. Issuer: {}, Audience: {}, Subject: {}, Email: {}",
                    jwt.getIssuer(),
                    jwt.getAudience(),
                    jwt.getSubject(),
                    jwt.getClaim("email").asString());

            String providerId = jwt.getSubject();
            log.info("Auth0JwtFilter: Setting providerId as principal: {}", providerId);

            // Use the providerId (Auth0 subject) as principal
            var authentication =
                    new UsernamePasswordAuthenticationToken(
                            providerId, null, Collections.emptyList());

            SecurityContextHolder.getContext().setAuthentication(authentication);

            chain.doFilter(req, res);
        } catch (Exception e) {
            log.warn("Auth0JwtFilter: JWT verification failed: {}", e.getMessage());
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.setContentType("application/json;charset=UTF-8");
            res.getWriter()
                    .write(
                            """
                        {"status":401,"error":"Unauthorized","code":"AUTH_INVALID_TOKEN","message":"Invalid or expired access token"}
                        """);
        }
    }
}
