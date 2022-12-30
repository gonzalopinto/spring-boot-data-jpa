package com.springboot.app.controllers;

import java.util.List;
import java.util.Locale;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.springboot.app.models.entity.Cliente;
import com.springboot.app.models.entity.Factura;
import com.springboot.app.models.entity.ItemFactura;
import com.springboot.app.models.entity.Producto;
import com.springboot.app.services.IClienteService;

@Secured("ROLE_ADMIN")
@Controller
@RequestMapping("/factura")
@SessionAttributes("factura")
public class FacturaController {

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private IClienteService clienteService;

	@Autowired
	private MessageSource messageSource;

	@GetMapping("/form/{clienteId}")
	public String crear(@PathVariable Long clienteId, Model m, RedirectAttributes flash, Locale locale)
	{
		Cliente c = clienteService.findOne(clienteId);
		if (c == null)
		{
			flash.addFlashAttribute("error", messageSource.getMessage("text.factura.flash.db.error", null, locale));
			return "redirect:/listar";
		}

		Factura f = new Factura();
		f.setCliente(c);

		m.addAttribute("factura", f);
		m.addAttribute("titulo", messageSource.getMessage("text.factura.form.titulo", null, locale));

		return "factura/form";
	}

	@GetMapping(value = "/cargar-productos/{term}", produces = "application/json")
	public @ResponseBody List<Producto> cargarProductos(@PathVariable String term)
	{
		return clienteService.findByNombre(term);
	}

	@PostMapping("/form")
	public String guardar(@Valid Factura factura, BindingResult result, Model m,
			@RequestParam(name = "item_id[]", required = false) Long[] itemId,
			@RequestParam(name = "cantidad[]", required = false) Integer[] cantidad, RedirectAttributes flash,
			SessionStatus status, Locale locale)
	{

		m.addAttribute("titulo", messageSource.getMessage("text.factura.form.titulo", null, locale));

		if (result.hasErrors())
		{
			return "factura/form";
		}

		if (itemId == null || itemId.length == 0)
		{
			m.addAttribute("titulo", messageSource.getMessage("text.factura.form.titulo", null, locale));
			m.addAttribute("error", messageSource.getMessage("text.factura.flash.lineas.error", null, locale));
			return "factura/form";
		}

		for (int i = 0; i < itemId.length; i++)
		{
			Producto p = clienteService.findProductById(itemId[i]);
			ItemFactura l = new ItemFactura();
			l.setCantidad(cantidad[i]);
			l.setProducto(p);

			factura.addItemFactura(l);

			log.debug("ID: " + itemId[i].toString() + " - cantidad: " + l.getCantidad());
		}

		clienteService.saveFactura(factura);
		status.setComplete();
		flash.addFlashAttribute("success", messageSource.getMessage("text.factura.flash.crear.success", null, locale));

		return "redirect:/ver/" + factura.getCliente().getId();
	}

	@GetMapping(value = "/ver/{id}")
	public String ver(@PathVariable Long id, Model m, RedirectAttributes flash, Locale locale)
	{
		Factura f = clienteService.fetchFacturaByIdWithClienteWithItemFacturaWithProducto(id);
		// clienteService.findFacturaById(id);
		if (f == null)
		{
			m.addAttribute("error", messageSource.getMessage("text.factura.flash.db.error", null, locale));
			return "redirect:/listar";
		}

		m.addAttribute("titulo",
				String.format(messageSource.getMessage("text.factura.ver.titulo", null, locale), f.getDescripcion()));
		m.addAttribute("factura", f);

		return "factura/ver";
	}

	@GetMapping(value = "/eliminar/{id}")
	public String eliminar(@PathVariable Long id, Model m, RedirectAttributes flash, Locale locale)
	{
		Factura f = clienteService.findFacturaById(id);
		if (f != null)
		{
			clienteService.deleteFactura(id);
			m.addAttribute("success", messageSource.getMessage("text.factura.flash.eliminar.success", null, locale));
			return "redirect:/ver/" + f.getCliente().getId();
		}

		m.addAttribute("error", messageSource.getMessage("text.factura.flash.db.error", null, locale));
		return "redirect:/listar";
	}

}
