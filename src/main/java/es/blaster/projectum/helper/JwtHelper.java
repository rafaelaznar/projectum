package es.blaster.projectum.helper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import es.blaster.projectum.exception.JWTException;
import java.util.ArrayList;

public class JwtHelper {

    private static final String SECRET = "1234qwerty1234qwerty1234qwerty1234qwerty";
    private static final String ISSUER = "BLASTER";

    private static SecretKey secretKey() {
        return Keys.hmacShaKeyFor((SECRET + ISSUER + SECRET).getBytes());
    }

    public static ArrayList<String> validateJWT(String strJWT) {
        Jws<Claims> headerClaimsJwt = Jwts.parserBuilder()
               .setSigningKey(secretKey())
               .build()
               .parseClaimsJws(strJWT);

        Claims claims = headerClaimsJwt.getBody();
        if (!claims.getIssuer().equals(ISSUER)) {
            throw new JWTException("Error validating JWT");
        }
        ArrayList<String> alCredentials = new ArrayList<>();
        alCredentials.add(claims.get("name", String.class));
        alCredentials.add(claims.get("type", String.class));
        return alCredentials;
    }
}
