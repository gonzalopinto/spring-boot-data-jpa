package com.springboot.app.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.springboot.app.models.entity.Producto;

public interface IProductoDao extends CrudRepository<Producto, Long>{

	@Query("SELECT p FROM Producto p WHERE p.nombre LIKE %?1%")
	//@Query("SELECT p FROM Producto p WHERE p.nombre LIKE %:term%")
	List<Producto> findByNombre(String term);
	
	List<Producto> findByNombreLikeIgnoreCase(String term);
	
}
