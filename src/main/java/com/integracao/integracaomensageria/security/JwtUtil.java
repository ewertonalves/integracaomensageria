package com.integracao.integracaomensageria.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {
	
	@Value("${teste.keykey}")
	private String senha;
	
	@Value("${jwt.expiracao}")
	private Long expiracaotoken;
	
	public String gerarToken(String usuario) {
		return Jwts.builder()
				.setSubject(usuario)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + expiracaotoken))
				.signWith(SignatureAlgorithm.HS256, senha)
                .compact();
	}
	
	public Claims extrairReividicacoes(String token) {
		return Jwts.parser()
                .setSigningKey(senha)
                .parseClaimsJws(token)
                .getBody();
	}
	
	public String extrairUsuario(String token) {
		return extrairReividicacoes(token).getSubject();
	}
	
	public boolean tokenExpirado(String token) {
		return extrairReividicacoes(token).getExpiration().before(new Date());
	}
	
	public boolean validadeToken(String token, String usuario) {
		return (usuario.equals(extrairUsuario(token)) && !tokenExpirado(token));
	}

}
