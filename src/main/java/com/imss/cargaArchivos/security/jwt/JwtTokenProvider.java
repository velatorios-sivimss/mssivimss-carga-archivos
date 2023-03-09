package com.imss.cargaArchivos.security.jwt;


import com.imss.cargaArchivos.util.AppConstantes;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class JwtTokenProvider {

	private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);


	@Value("${jwt.secretkey-dominios}")
	private String jwtSecret;


	
	public Long getUserIdFromJWT(String token) {
		Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();

		return Long.parseLong(claims.getSubject());
	}

	public String getUsernameFromToken(String token) {
		Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();

		return claims.getSubject();
	}

	public boolean validateToken(String authToken, HttpServletRequest request) {
		try {
			// Jwt token has not been tampered with
			Jws<Claims> claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
			return true;
		} catch (MalformedJwtException e) {
			request.setAttribute(AppConstantes.STATUSEXCEPTION, AppConstantes.MALFORMEDJWTEXCEPTION);
			return false;
		} catch (UnsupportedJwtException e) {
			logger.error("token no soportado");
			request.setAttribute(AppConstantes.STATUSEXCEPTION, AppConstantes.UNSUPPORTEDJWTEXCEPTION);
			return false;
		} catch (ExpiredJwtException e) {
			logger.error("token expirado");
			request.setAttribute(AppConstantes.STATUSEXCEPTION, AppConstantes.EXPIREDJWTEXCEPTION);
			return false;
		} catch (IllegalArgumentException e) {
			logger.error("token vacío {}",e.getMessage());
			request.setAttribute(AppConstantes.STATUSEXCEPTION, AppConstantes.ILLEGALARGUMENTEXCEPTION);
			return false;
		} catch (SignatureException e) {
			request.setAttribute(AppConstantes.STATUSEXCEPTION, AppConstantes.SIGNATUREEXCEPTION);
			return false;
		}catch (Exception e) {
			request.setAttribute(AppConstantes.STATUSEXCEPTION, AppConstantes.FORBIDDENEXCEPTION);
			return false;
		}
	}

}
