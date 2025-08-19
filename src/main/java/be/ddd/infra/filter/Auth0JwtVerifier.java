package be.ddd.infra.filter;

import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.JwkProviderBuilder;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.net.URL;
import java.security.interfaces.RSAPublicKey;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Auth0JwtVerifier {

    private final String issuer;
    private final String audience;
    private JwkProvider jwkProvider;

    public Auth0JwtVerifier(
            @Value("${auth0.issuer}") String issuer,
            @Value("${auth0.audience}") String audience,
            @Value("${auth0.domain}") String domain)
            throws Exception {
        this.issuer = issuer;
        this.audience = audience;
        this.jwkProvider =
                new JwkProviderBuilder(new URL("https://" + domain + "/.well-known/jwks.json"))
                        .cached(10, 24, TimeUnit.HOURS)
                        .rateLimited(10, 1, TimeUnit.MINUTES)
                        .build();
    }

    public DecodedJWT verify(String token) throws Exception {
        DecodedJWT unverified = JWT.decode(token);
        var jwk = jwkProvider.get(unverified.getKeyId());
        var publicKey = (RSAPublicKey) jwk.getPublicKey();
        var alg = Algorithm.RSA256(publicKey, null);

        JWTVerifier verifier =
                JWT.require(alg).withIssuer(issuer).withAudience(audience).acceptLeeway(3).build();

        return verifier.verify(token);
    }
}
