package manada.expensetracker.utils;

import ch.qos.logback.classic.spi.LoggingEventVO;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JWTUtil {

    public  String extractUserName(String token) {
        return extractClaim(token,Claims::getSubject);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }
     public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);
     }

     private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims =extractAllClaims(token);
        return claimsResolvers.apply(claims);
     }

     private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder().setClaims(extraClaims).setSubject(userDetails.getUsername()).setIssuedAt(new Date(System.currentTimeMillis())).setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256).compact();
     }
     public String generateRefreshToken(Map<String, Object> extraClaims,UserDetails userDetails) {
        return Jwts.builder().setClaims(extraClaims).setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+604800000))
                .signWith(getSigningKey(),SignatureAlgorithm.HS256).compact();
     }

     private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
     }
     private Date extractExpiration(String token) {
        return extractClaim(token,Claims::getExpiration);
     }
     private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token)
                .getBody();
     }

    private Key getSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode("413F442B472B846250653586560597033733676397924422645294484046351");
        return Keys.hmacShaKeyFor(keyBytes);
    }


}
