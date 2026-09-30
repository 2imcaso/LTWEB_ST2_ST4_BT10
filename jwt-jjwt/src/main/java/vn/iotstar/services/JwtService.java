package vn.iotstar.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    private final SecretKey signingKey;
    private final long jwtExpiration;
    public JwtService(@Value("${security.jwt.secret-key}") String secretKey,
                      @Value("${security.jwt.expiration-time}") long jwtExpiration) {
        this.signingKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)); this.jwtExpiration = jwtExpiration;
    }
    public String extractUsername(String token) { return extractClaim(token, Claims::getSubject); }
    public <T> T extractClaim(String token, Function<Claims, T> resolver) { return resolver.apply(extractAllClaims(token)); }
    public String generateToken(UserDetails userDetails) { return generateToken(new HashMap<>(), userDetails); }
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) { return buildToken(extraClaims, userDetails, jwtExpiration); }
    public long getExpirationTime() { return jwtExpiration; }
    private String buildToken(Map<String, Object> claims, UserDetails user, long expiration) {
        return Jwts.builder().claims(claims).subject(user.getUsername()).issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration)).signWith(signingKey, Jwts.SIG.HS256).compact();
    }
    public boolean isTokenValid(String token, UserDetails user) { return extractUsername(token).equals(user.getUsername()) && !isTokenExpired(token); }
    private boolean isTokenExpired(String token) { return extractClaim(token, Claims::getExpiration).before(new Date()); }
    private Claims extractAllClaims(String token) { return Jwts.parser().verifyWith(signingKey).build().parseSignedClaims(token).getPayload(); }
}
