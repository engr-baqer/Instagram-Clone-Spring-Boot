package devsinc.Instagram.clone.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


/**
 * This class has been made to extract the UserDetails from JWT Token
 * This class will deal with all the manipulations related to Jwt Token
 */

@Service
public class JwtService {

    private final static String JWT_KEY = "zgB+ga27KDKf6A8Yd3+fNEs7rJ5rh72b8GqezICH2HRgH9Jb6qk5lVjKqX7Y0oR0\n";

    /**
     * This function will take the JWT token and extract the username from the token
     * @param token This parameter is the JWT token passed to the function
     * @return This function will return the username extracted from the JWT token.
     */
    public String extractUsername(String token) {
        return extractSingleClaim(token, Claims::getSubject);
    }

    /**
     * This function will extract all the claims which contains by a JWT token
     * @param token This parameter is the JWT token passed to the function
     * @return This function is return a Claims type object will contain all the claims of JWT token
     */
    private Claims extractAllClaims(String token)
    {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * This method will convert our secret JWT_KEY into hmacShaKeyFor() key.
     *
     * @return This function returns a key of Sha256
     */
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(JWT_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * This method is a generic function for extracting a specific claim from a JWT (JSON Web Token) token.
     *
     * @param token           The JWT token from which to extract the claim.
     * @param claimsSeparator A function that takes a JWT Claims object and extracts the desired claim.
     * @param <T>             The type of the claim to be extracted.
     * @return The extracted claim of type T.
     */
    public <T> T extractSingleClaim(String token, Function<Claims, T> claimsSeparator)
    {
        final Claims claims = extractAllClaims(token);
        return claimsSeparator.apply(claims);
    }

    /**
     * Generates a JWT (JSON Web Token) with additional custom claims.
     *
     * @param extraClaims  A map of additional claims to include in the JWT payload.
     * @param userDetails  The user details to associate with the JWT.
     * @return The generated JWT as a string.
     */
    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ){
        return Jwts
                .builder()
                .setHeaderParam("alg", "HS256")
                .setHeaderParam("typ", "JWT")
                .setClaims(extraClaims)
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .setSubject(userDetails.getUsername())
                .compact();
    }
    /**
     * Generates a JWT (JSON Web Token) using the provided user details, with no additional custom claims.
     *
     * @param userDetails The user details to associate with the JWT.
     * @return The generated JWT as a string.
     */
    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(), userDetails);
    }
    /**
     * Validates a JWT (JSON Web Token) to check if it is valid or not by comparing the token's username claim
     * with the username from the provided user details.
     *
     * @param token        The JWT token to be validated.
     * @param userDetails  The user details against which the token's username will be compared.
     * @return True if the token is valid and matches the user details; otherwise, false.
     */
    public boolean isTokenValid(String token, UserDetails userDetails)
    {
        String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()));
    }

}
