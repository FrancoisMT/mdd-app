package mdd_api.mdd_api.services;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JWTService {
	
	 @Value("${security.app.jwtSecret}")
	 private String secretKey;
	
	 @Value("${security.app.jwtExpirationMs}")
	 private long jwtExpiration;
	 
	 public String extractUserMail(String token) {	
			return extractClaims(token, Claims::getSubject);
	 }
	 
	 public boolean isTokenValid(String token, UserDetails userDetails) {
	        final String username = extractUserMail(token);
	        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
	 }
	 
	 private boolean isTokenExpired(String token) {
	        return extractExpiration(token).before(new Date());
	 }
	 
	 private Date extractExpiration(String token) {
	        return extractClaims(token, Claims::getExpiration);
	 }
	 
	 public long getExpirationTime() {
	        return jwtExpiration;
	 }
	 
	 public String generateToken(UserDetails userDetails) {
	        return generateToken(new HashMap<>(), userDetails);
	 }
	 
	 public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
	        return buildToken(extraClaims, userDetails, jwtExpiration);
	 }

	 private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration) {
		 
		 String token = Jwts
				 			.builder()
				 			.subject(userDetails.getUsername())
				 			.issuedAt(new Date(System.currentTimeMillis()))
				 			.expiration(new Date(System.currentTimeMillis() + expiration))
				 			.signWith(getSigninKey())
				 			.compact();
	                
		return token;
		 
	 }
	
	 public <T> T extractClaims(String token, Function<Claims, T> resolver) {
			Claims claims = extractAllClaims(token);
			return resolver.apply(claims);
	 }
	 
	 private Claims extractAllClaims(String token) {
			
			return Jwts
					.parser()
					.verifyWith(getSigninKey())
					.build()
					.parseSignedClaims(token)
					.getPayload();
			
	}
	 
	private SecretKey getSigninKey() {
			byte[] keyBytes = Decoders.BASE64URL.decode(secretKey);
			return Keys.hmacShaKeyFor(keyBytes);
	}




	 	
}