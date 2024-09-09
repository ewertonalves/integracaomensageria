package com.integracao.integracaomensageria.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.integracao.integracaomensageria.model.Usuario;
import com.integracao.integracaomensageria.repository.UsuarioRepository;

@Service
public class DetalhesUsuarioService implements UserDetailsService{
	
	private final UsuarioRepository repository;
	
	public DetalhesUsuarioService(UsuarioRepository repository) {
		this.repository = repository;
	}

	@Override
	public UserDetails loadUserByUsername(String usuario) throws UsernameNotFoundException {
		Usuario user = repository.findByUsuario(usuario);
		if(user == null)
			throw new UsernameNotFoundException("Usuário não encontrado");
        return new org.springframework.security.core.userdetails.User(user.getUsuario(), user.getSenha(), false, false, false, false, null);
	}

}
