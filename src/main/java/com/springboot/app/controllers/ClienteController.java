package com.springboot.app.controllers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.springboot.app.controllers.util.paginator.PageRender;
import com.springboot.app.models.entity.Cliente;
import com.springboot.app.services.IClienteService;
import com.springboot.app.services.IUploadService;
import com.springboot.app.view.xml.ClienteList;

@Controller
// Mejor que el hidden id, usar SessionAtributes con el SessionStatus en el metodo correspondiente
@SessionAttributes("cliente")
public class ClienteController {

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private IClienteService clienteService;

	@Autowired
	private IUploadService uploadService;

	@Autowired
	private MessageSource messageSource;

	@GetMapping(
	{
			"/listar", "/"
	})
	public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model,
			Authentication authentication, HttpServletRequest request, Locale locale)
	{

		// Maneras de tener lo mismo
		if (authentication != null)
		{
			log.info("Usuario: '" + authentication.getName() + "' - autenticado");
		}
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null)
		{
			log.info("Usuario: '" + auth.getName() + "' - autenticado 2");
		}

		if (hasRole("ROLE_ADMIN"))
			log.info("hasRole: Hola " + auth.getName() + " tienes acceso!");
		else
			log.info("hasRole: Hola " + auth.getName() + " NO tienes acceso!");

		SecurityContextHolderAwareRequestWrapper scharw = new SecurityContextHolderAwareRequestWrapper(request,
				"ROLE_");
		if (scharw.isUserInRole("ADMIN"))
			log.info("SecurityContextHolderAwareRequestWrapper: Hola " + auth.getName() + " tienes acceso!");
		else
			log.info("SecurityContextHolderAwareRequestWrapper: Hola " + auth.getName() + " NO tienes acceso!");

		if (request.isUserInRole("ROLE_ADMIN"))
			log.info("request: Hola " + auth.getName() + " tienes acceso!");
		else
			log.info("request: Hola " + auth.getName() + " NO tienes acceso!");

		Pageable pageRequest = PageRequest.of(page, 5);
		Page<Cliente> clientes = clienteService.findAll(pageRequest);
		PageRender<Cliente> pRender = new PageRender<>("/listar", clientes);

		model.addAttribute("titulo", messageSource.getMessage("text.cliente.listar.titulo", null, locale));
		model.addAttribute("clientes", clientes);
		model.addAttribute("page", pRender);
		return "listar";
	}

	// para que spring envie la extension ademas del nombre se pone :.+
	@GetMapping(value = "/uploads/{filename:.+}")
	@Secured("ROLE_USER")
	public ResponseEntity<Resource> verFoto(@PathVariable String filename)
	{
		Resource r = null;
		try
		{
			r = uploadService.load(filename);
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
		}
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + r.getFilename() + "\"").body(r);
	}

	// Podemos hacerlo con Secured o PreAuthorize, habrá que indicarlo en el config
	// @Secured("ROLE_USER")
	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping("/ver/{id}")
	public String ver(@PathVariable Long id, Model m, RedirectAttributes flash, Locale locale)
	{
		Cliente c = clienteService.fetchClienteByIdWithFacturas(id);
		// clienteService.findOne(id);
		if (c == null)
		{
			flash.addFlashAttribute("error", "El cliente no existe en la BBDD.");
			return "redirect:/listar";
		}
		m.addAttribute("cliente", c);
		m.addAttribute("titulo", messageSource.getMessage("text.cliente.detalle.titulo", null, locale).concat(": ")
				.concat(c.getNombre()));

		log.info("imagePath: " + c.getFoto());

		return "ver";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/form")
	public String crear(Map<String, Object> model, Locale locale)
	{
		model.put("titulo", messageSource.getMessage("text.cliente.form.titulo.crear", null, locale));

		Cliente cliente = new Cliente();
		model.put("cliente", cliente);

		return "form";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/form", method = RequestMethod.POST)
	public String guardar(@Valid Cliente cliente, BindingResult result, Model m,
			@RequestParam("file") MultipartFile foto, RedirectAttributes flash, SessionStatus status, Locale locale)
	{
		if (result.hasErrors())
		{
			m.addAttribute("titulo", messageSource.getMessage("text.cliente.form.titulo", null, locale));

			return "form";
		}
		if (!foto.isEmpty())
		{
			// Path dirRecursos = Paths.get("src//main//resources///static///uploads");
			// String rootPath = dirRecursos.toFile().getAbsolutePath();

			if (cliente.getId() != null && cliente.getId() > 0 && cliente.getFoto() != null
					&& !cliente.getFoto().isEmpty())
			{

				uploadService.delete(cliente.getFoto());
			}

			String uniqueFilename = null;
			try
			{
				uniqueFilename = uploadService.copy(foto);
				flash.addFlashAttribute("info",
						messageSource.getMessage("text.cliente.flash.foto.subir.success", null, locale) + "'"
								+ uniqueFilename + "'");

				// cliente.setFoto(foto.getOriginalFilename());
				cliente.setFoto(uniqueFilename);

			}
			catch (IOException e)
			{
				e.printStackTrace();
			}

		}

		String mensajeFlash = (cliente.getId() != null)
				? messageSource.getMessage("text.cliente.flash.editar.success", null, locale)
				: messageSource.getMessage("text.cliente.flash.crear.success", null, locale);

		clienteService.save(cliente);
		// para decir que se ha completado el tiempo de vida de la sesion
		status.setComplete();
		flash.addFlashAttribute("success", mensajeFlash);
		return "redirect:listar";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/form/{id}")
	public String editar(Map<String, Object> model, @PathVariable Long id, RedirectAttributes flash, Locale locale)
	{
		model.put("titulo", "Formulario Cliente");
		Cliente cliente = null;
		if (id > 0)
		{
			cliente = clienteService.findOne(id);
			if (cliente == null)
			{
				flash.addFlashAttribute("error", messageSource.getMessage("text.cliente.flash.db.error", null, locale));
				return "redirect:/listar";
			}
		}
		else
		{
			flash.addFlashAttribute("error", messageSource.getMessage("text.cliente.flash.id.error", null, locale));
			return "redirect:/listar";
		}

		model.put("cliente", cliente);
		model.put("titulo", messageSource.getMessage("text.cliente.form.titulo.editar", null, locale));

		return "form";
	}

	@Secured("ROLE_ADMIN")
	// Valen los 2, requestmapping es GET por defecto
	// @GetMapping(value = "/eliminar/{id}")
	@RequestMapping(value = "/eliminar/{id}")
	public String eliminar(Map<String, Object> model, @PathVariable Long id, RedirectAttributes flash, Locale locale)
	{
		model.put("titulo", "Eliminar Cliente");
		if (id > 0)
		{
			Cliente c = clienteService.findOne(id);

			clienteService.delete(id);
			flash.addFlashAttribute("success",
					messageSource.getMessage("text.cliente.flash.eliminar.success", null, locale));

			if (uploadService.delete(c.getFoto()))
			{
				String mensajeFotoEliminar = String.format(
						messageSource.getMessage("text.cliente.flash.foto.eliminar.success", null, locale),
						c.getFoto());
				flash.addFlashAttribute("info", mensajeFotoEliminar);
			}
		}

		return "redirect:/listar";
	}

	private boolean hasRole(String rol)
	{

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null)
		{
			return false;
		}

		Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();

		return authorities.contains(new SimpleGrantedAuthority(rol));
		// return authorities.stream().anyMatch(a -> a.getAuthority().equals(rol));
	}

	@GetMapping("/listar-rest")
	public @ResponseBody ClienteList listarRest(@RequestParam(name = "page", defaultValue = "0") int page, Model model,
			Authentication authentication, HttpServletRequest request, Locale locale)
	{
		return new ClienteList(clienteService.findAll());
	}

}
