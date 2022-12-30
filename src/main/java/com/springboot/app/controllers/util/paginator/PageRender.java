package com.springboot.app.controllers.util.paginator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;

public class PageRender<T> {

	private String url;

	private Page<T> page;

	private int totalPaginas;

	private int numElemsPagina;

	private int paginaActual;

	private List<PageItem> paginas;

	public PageRender(String url, Page<T> page) {
		super();
		this.url = url;
		this.page = page;
		this.paginas = new ArrayList<>();

		numElemsPagina = page.getSize();
		totalPaginas = page.getTotalPages();
		paginaActual = page.getNumber() + 1;

		int desde, hasta;
		if (totalPaginas <= numElemsPagina) {
			desde = 1;
			hasta = totalPaginas;
		} else {
			if (paginaActual <= numElemsPagina / 2) {
				desde = 1;
				hasta = numElemsPagina;
			} else if (paginaActual >= totalPaginas - numElemsPagina / 2) {
				desde = totalPaginas - numElemsPagina + 1;
				hasta = numElemsPagina;
			} else {
				desde = paginaActual - numElemsPagina / 2;
				hasta = numElemsPagina;
			}
		}

		for (int i = 0; i < hasta; i++) {
			paginas.add(new PageItem(desde + i, paginaActual == desde + i));
		}

	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Page<T> getPage() {
		return this.page;
	}

	public void setPage(Page<T> page) {
		this.page = page;
	}

	public int getTotalPaginas() {
		return this.totalPaginas;
	}

	public void setTotalPaginas(int totalPaginas) {
		this.totalPaginas = totalPaginas;
	}

	public int getNumElemsPagina() {
		return this.numElemsPagina;
	}

	public void setNumElemsPagina(int numElemsPagina) {
		this.numElemsPagina = numElemsPagina;
	}

	public int getPaginaActual() {
		return this.paginaActual;
	}

	public void setPaginaActual(int paginaActual) {
		this.paginaActual = paginaActual;
	}

	public List<PageItem> getPaginas() {
		return this.paginas;
	}

	public void setPaginas(List<PageItem> paginas) {
		this.paginas = paginas;
	}

	public boolean isFirst() {
		return page.isFirst();
	}

	public boolean isLast() {
		return page.isLast();
	}

	public boolean hasNext() {
		return page.hasNext();
	}

	public boolean hasPrevious() {
		return page.hasPrevious();
	}
}
