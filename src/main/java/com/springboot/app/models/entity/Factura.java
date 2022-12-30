package com.springboot.app.models.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "facturas")
public class Factura implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotEmpty
	private String descripcion;

	private String observacion;

	@Temporal(TemporalType.DATE)
	@Column(name = "create_at")
	private Date createAt;

	@ManyToOne(fetch = FetchType.LAZY)
	@JsonBackReference
	private Cliente cliente;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "factura_id")
	private List<ItemFactura> items;

	@PrePersist
	public void PrePersist()
	{
		createAt = new Date();
	}

	public Factura()
	{
		super();
		this.items = new ArrayList<>();
	}

	public Long getId()
	{
		return this.id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getDescripcion()
	{
		return this.descripcion;
	}

	public void setDescripcion(String descripcion)
	{
		this.descripcion = descripcion;
	}

	public String getObservacion()
	{
		return this.observacion;
	}

	public void setObservacion(String observacion)
	{
		this.observacion = observacion;
	}

	public Date getCreateAt()
	{
		return this.createAt;
	}

	public void setCreateAt(Date createAt)
	{
		this.createAt = createAt;
	}

	@XmlTransient
	public Cliente getCliente()
	{
		return this.cliente;
	}

	public void setCliente(Cliente cliente)
	{
		this.cliente = cliente;
	}

	public List<ItemFactura> getItems()
	{
		return this.items;
	}

	public void setItems(List<ItemFactura> items)
	{
		this.items = items;
	}

	public void addItemFactura(ItemFactura i)
	{
		this.items.add(i);
	}

	public Double getTotal()
	{
		return items.stream().mapToDouble(ItemFactura::calcularImporte).sum();
	}

}
