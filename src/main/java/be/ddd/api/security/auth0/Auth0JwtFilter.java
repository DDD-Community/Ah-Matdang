package be.ddd.api.security.auth0;

import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class Auth0JwtFilter extends OncePerRequestFilter {

    private final Auth0JwtVerifier verifier;

    public Auth0JwtFilter(Auth0JwtVerifier verifier) {
        this.verifier = verifier;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
        throws ServletException, IOException {
        String auth = req.getHeader("Authorization");
        if (!StringUtils.hasText(auth) || !auth.startsWith("Bearer ")) {
            chain.doFilter(req, res);
            return;
        }

        String token = auth.substring(7);
        try {
            DecodedJWT jwt = verifier.verify(token);
            req.setAttribute("auth0.sub", jwt.getSubject());
            req.setAttribute("auth0.email", jwt.getClaim("email").asString());
            chain.doFilter(req, res);
        } catch (Exception e) {
            chain.doFilter(req, res);
        }
    }

}
