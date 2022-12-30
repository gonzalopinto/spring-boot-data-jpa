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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "clientes")
public class Cliente implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	// @Column(columnDefinition = "id BIGINT GENERATED BY DEFAULT AS IDENTITY DEFAULT ON NULL PRIMARY KEY")
	private Long id;

	@NotEmpty
	// @Column(name = "NOMBRE_CLIENTE", nullable = true)
	private String nombre;

	@NotEmpty
	@Column(name = "APELLIDO")
	private String apellidos;

	@NotEmpty
	@Email
	private String email;

	@NotNull
	@Column(name = "create_at")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createAt;

	@OneToMany(mappedBy = "cliente", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference
	private List<Factura> facturas;

	private String foto;

	// @PrePersist
	// public void prePersist() {
	// createAt = new Date();
	// }

	public Cliente()
	{
		this.facturas = new ArrayList<>();
	}

	public Long getId()
	{
		return this.id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getNombre()
	{
		return this.nombre;
	}

	public void setNombre(String nombre)
	{
		this.nombre = nombre;
	}

	public String getApellidos()
	{
		return this.apellidos;
	}

	public void setApellidos(String apellidos)
	{
		this.apellidos = apellidos;
	}

	public String getEmail()
	{
		return this.email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public Date getCreateAt()
	{
		return this.createAt;
	}

	public void setCreateAt(Date createAt)
	{
		this.createAt = createAt;
	}

	public String getFoto()
	{
		return this.foto;
	}

	public void setFoto(String foto)
	{
		this.foto = foto;
	}

	public List<Factura> getFacturas()
	{
		return facturas;
	}

	public void setFacturas(List<Factura> facturas)
	{
		this.facturas = facturas;
	}

	public void addFactura(Factura f)
	{
		this.facturas.add(f);
	}

	@Override
	public String toString()
	{
		return this.nombre + " " + this.apellidos;
	}

}
