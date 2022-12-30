package com.springboot.app.view.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.springboot.app.models.entity.Cliente;

@XmlRootElement(name = "clienteList")
public class ClienteList {

	@XmlElement(name = "cliente")
	public List<Cliente> clientes;

	public ClienteList(List<Cliente> clientes)
	{
		this.clientes = clientes;
	}

	public ClienteList()
	{
	}

	public List<Cliente> getClientes()
	{
		return this.clientes;
	}

}
