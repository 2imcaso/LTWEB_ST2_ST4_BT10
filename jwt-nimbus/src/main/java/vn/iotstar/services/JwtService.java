package vn.iotstar.services;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {
    private final byte[] signingKey;
    private final long jwtExpiration;
    public JwtService(@Value("${security.jwt.secret-key}") String secretKey,
                      @Value("${security.jwt.expiration-time}") long jwtExpiration) {
        this.signingKey = secretKey.getBytes(StandardCharsets.UTF_8); this.jwtExpiration = jwtExpiration;
    }
    public String extractUsername(String token) throws ParseException, JOSEException { return verified(token).getJWTClaimsSet().getSubject(); }
    public String generateToken(UserDetails userDetails) { return generateToken(new HashMap<>(), userDetails); }
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) { return buildToken(extraClaims, userDetails, jwtExpiration); }
    public long getExpirationTime() { return jwtExpiration; }
    private String buildToken(Map<String, Object> claims, UserDetails user, long expiration) {
        try {
            JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder().subject(user.getUsername()).issueTime(new Date())
                    .expirationTime(new Date(System.currentTimeMillis() + expiration));
            claims.forEach(builder::claim);
            SignedJWT jwt = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), builder.build());
            jwt.sign(new MACSigner(signingKey)); return jwt.serialize();
        } catch (JOSEException ex) { throw new IllegalStateException("Khong the tao JWT", ex); }
    }
    public boolean isTokenValid(String token, UserDetails user) throws ParseException, JOSEException {
        SignedJWT jwt = verified(token);
        return user.getUsername().equals(jwt.getJWTClaimsSet().getSubject())
                && jwt.getJWTClaimsSet().getExpirationTime().after(new Date());
    }
    private SignedJWT verified(String token) throws ParseException, JOSEException {
        SignedJWT jwt = SignedJWT.parse(token);
        if (!JWSAlgorithm.HS256.equals(jwt.getHeader().getAlgorithm()) || !jwt.verify(new MACVerifier(signingKey)))
            throw new IllegalArgumentException("JWT khong hop le");
        return jwt;
    }
}
