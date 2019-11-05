package br.gov.sp.prodesp.ssp.dipol.enderecoservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.sp.prodesp.ssp.dipol.enderecoservice.domain.entity.LogradouroTipo;
import br.gov.sp.prodesp.ssp.dipol.enderecoservice.repository.LogradouroTipoRepository;

@Service
public class LogradouroTipoService {

	@Autowired
	private LogradouroTipoRepository logradouroTipoRepository;

	public LogradouroTipo findByDescricao(String tipo) {
		return logradouroTipoRepository.findByDescricao(tipo);
	}

}
