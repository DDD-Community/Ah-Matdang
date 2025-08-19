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
import org.springframework.security.core.userdetails.User;
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
        if (!StringUtils.hasText(auth) || !auth.startsWith("Bearer ")) {
            chain.doFilter(req, res);
            return;
        }

        String token = auth.substring(7);
        try {
            DecodedJWT jwt = verifier.verify(token);

            req.setAttribute("auth0.jwt", jwt);
            req.setAttribute("auth0.sub", jwt.getSubject());
            req.setAttribute("auth0.email", jwt.getClaim("email").asString());

            var principal = new User(jwt.getSubject(), "", Collections.emptyList());
            var authentication =
                    new UsernamePasswordAuthenticationToken(
                            principal, null, principal.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            chain.doFilter(req, res);
        } catch (Exception e) {
            log.warn("JWT verification failed: {}", e.getMessage());
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
