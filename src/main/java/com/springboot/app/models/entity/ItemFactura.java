package com.springboot.app.models.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "facturas_items")
public class ItemFactura implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Integer cantidad;

	@ManyToOne(fetch = FetchType.LAZY)
	// se podria omitir la de abajo
	@JoinColumn(name = "producto_id")
	@JsonIgnoreProperties(
	{
			"hibernateLazyInitializer", "handler"
	})
	private Producto producto;

	public Long getId()
	{
		return this.id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public Integer getCantidad()
	{
		return this.cantidad;
	}

	public void setCantidad(Integer cantidad)
	{
		this.cantidad = cantidad;
	}

	public Double calcularImporte()
	{
		return cantidad.doubleValue() * producto.getPrecio();
	}

	public Producto getProducto()
	{
		return this.producto;
	}

	public void setProducto(Producto producto)
	{
		this.producto = producto;
	}

}
