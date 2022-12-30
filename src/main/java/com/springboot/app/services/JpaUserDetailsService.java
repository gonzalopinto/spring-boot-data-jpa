package com.springboot.app.services;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.app.models.dao.IUsuarioDao;
import com.springboot.app.models.entity.Role;
import com.springboot.app.models.entity.Usuario;

@Service
public class JpaUserDetailsService implements UserDetailsService {

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private IUsuarioDao usuarioDao;

	@Transactional(readOnly = true)
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
	{
		Usuario usuario = usuarioDao.findByUsername(username);

		if (usuario == null)
		{
			log.error("Error login: No existe el usuario '" + username + "'");
			throw new UsernameNotFoundException("Error login: No existe el usuario '" + username + "'");
		}

		List<GrantedAuthority> auths = new ArrayList<>();
		for (Role r : usuario.getRoles())
		{
			log.info("Rol: ".concat(r.getAuthority()));
			auths.add(new SimpleGrantedAuthority(r.getAuthority()));
		}

		if (auths.isEmpty())
		{
			log.error("Error login: No existe el usuario '" + username + "' no tiene roles asignados!");
			throw new UsernameNotFoundException(
					"Error login: No existe el usuario '" + username + "' no tiene roles asignados!");
		}

		return new User(username, usuario.getPwd(), usuario.getEnabled(), true, true, true, auths);
	}

}
