package com.springboot.app;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import com.springboot.app.view.xml.ClienteList;

@Configuration
public class MVCConfig implements WebMvcConfigurer {

	private final Logger log = LoggerFactory.getLogger(getClass());

	// @Override
	// public void addResourceHandlers(ResourceHandlerRegistry registry) {
	// WebMvcConfigurer.super.addResourceHandlers(registry);
	//
	// String resourcePath = Paths.get("uploads").toAbsolutePath().toUri().toString();
	// registry.addResourceHandler("/uploads/**").addResourceLocations(resourcePath);
	//
	// log.info("resourcePath: " + resourcePath);
	//
	// }

	public void addViewControllers(ViewControllerRegistry v)
	{
		v.addViewController("/error_403").setViewName("error_403");
	}

	@Bean
	public LocaleResolver localeResolver()
	{
		SessionLocaleResolver slr = new SessionLocaleResolver();
		slr.setDefaultLocale(new Locale("es", "ES"));

		return slr;
	}

	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor()
	{
		LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
		lci.setParamName("lang");
		return lci;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry)
	{
		registry.addInterceptor(localeChangeInterceptor());
	}

	@Bean
	public Jaxb2Marshaller jaxb2Marshaller()
	{
		Jaxb2Marshaller jm = new Jaxb2Marshaller();
		jm.setClassesToBeBound(new Class[]
		{
				ClienteList.class
		});

		return jm;
	}

}
