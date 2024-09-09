package com.integracao.integracaomensageria.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.integracao.integracaomensageria.model.MessageResponse;
import com.integracao.integracaomensageria.model.Usuario;
import com.integracao.integracaomensageria.security.JwtUtil;
import com.integracao.integracaomensageria.service.DetalhesUsuarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/autenticacao")
public class AutenticacaoController {
	
	private final JwtUtil util;
	private final DetalhesUsuarioService service;
	
	public AutenticacaoController(JwtUtil util, DetalhesUsuarioService service) {
		this.util	 = util;
		this.service = service;
	}
	
	@PostMapping(value = "/login", consumes = "application/json; charset=UTF-8")
	public ResponseEntity<MessageResponse> login(@Valid @RequestBody Usuario usuario) {
		try {
			UserDetails userDetails = service.loadUserByUsername(usuario.getUsuario());
			if(userDetails != null && userDetails.getPassword().equals(usuario.getSenha())) {
				String token = util.gerarToken(userDetails.getUsername());
				return ResponseEntity.ok(new MessageResponse(token));
			}else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Usuario não autorizado."));
			}
		} catch (Exception e) {
			 return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Ocorreu um erro."));
		}
	}


}
