package com.comsense.assignment.utils;


import com.comsense.assignment.exceptions.JwtTokenMalformedException;
import com.comsense.assignment.exceptions.JwtTokenMissingException;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Set;

@Component
public class JwtTokenUtil {

	@Value("${jwt.secret}")
	private String jwtSecret;

	public String generateToken(String id, Set<String> roles) {
		Claims claims = Jwts.claims().setSubject(id);
		claims.put("roles", roles);
		long nowMillis = System.currentTimeMillis();
		long expMillis = System.currentTimeMillis() + 1000 * 60 * 60 * 100;
		Date exp = new Date(expMillis);
		return Jwts.builder().setClaims(claims).setIssuedAt(new Date(nowMillis)).setExpiration(exp)
				.signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
	}

	public void validateToken(final String token) throws JwtTokenMalformedException, JwtTokenMissingException {
		try {
			Jwts.parserBuilder().setSigningKey(jwtSecret).build().parseClaimsJws(token);
		} catch (SignatureException ex) {
			throw new JwtTokenMalformedException("Invalid JWT signature");
		} catch (MalformedJwtException ex) {
			throw new JwtTokenMalformedException("Invalid JWT token");
		} catch (ExpiredJwtException ex) {
			throw new JwtTokenMalformedException("Expired JWT token");
		} catch (UnsupportedJwtException ex) {
			throw new JwtTokenMalformedException("Unsupported JWT token");
		} catch (IllegalArgumentException ex) {
			throw new JwtTokenMissingException("JWT claims string is empty.");
		}
	}
}
