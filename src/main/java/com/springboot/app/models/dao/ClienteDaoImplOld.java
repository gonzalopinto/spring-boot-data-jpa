package com.springboot.app.models.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.app.models.entity.Cliente;

//@Repository // ("clienteDaoJPA")
public class ClienteDaoImplOld {// implements IClienteDao {
//
//	@PersistenceContext
//	private EntityManager em;
//
//	@SuppressWarnings("unchecked")
//	// @Transactional(readOnly = true)
//	@Override
//	public List<Cliente> findAll() {
//		return em.createQuery("from Cliente").getResultList();
//	}
//
//	// @Transactional(readOnly = true)
//	@Override
//	public Cliente findOne(Long id) {
//		return em.find(Cliente.class, id);
//	}
//
//	// @Transactional
//	@Override
//	public void save(Cliente cliente) {
//		if (cliente.getId() != null && cliente.getId() > 0) {
//			// Si existe, actualizo
//			em.merge(cliente);
//		} else {
//			// Si NO existe, lo creo
//			em.persist(cliente);
//		}
//	}
//
//	// @Transactional
//	@Override
//	public void delete(Long id) {
//		em.remove(findOne(id));
//	}

}
