package com.springboot.app.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.springboot.app.models.entity.Cliente;
import com.springboot.app.models.entity.Factura;
import com.springboot.app.models.entity.Producto;

public interface IClienteService {

	List<Cliente> findAll();
	
	Page<Cliente> findAll(Pageable pageable);

	void save(Cliente cliente);

	Cliente findOne(Long id);

	void delete(Long id);
	
	List<Producto> findByNombre(String term);
	
	void saveFactura(Factura factura);
	
	Producto findProductById(Long id);
	 
	Factura findFacturaById(Long id);
	
	void deleteFactura(Long id);
	
	Factura fetchFacturaByIdWithClienteWithItemFacturaWithProducto(Long id);
	
	Cliente fetchClienteByIdWithFacturas(Long id);
}
