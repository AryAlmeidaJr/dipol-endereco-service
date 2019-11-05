package br.gov.sp.prodesp.ssp.dipol.enderecoservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.sp.prodesp.ssp.dipol.enderecoservice.domain.entity.Cep;
import br.gov.sp.prodesp.ssp.dipol.enderecoservice.repository.CepRepository;

@Service
public class CepService {

	@Autowired
	private CepRepository cepRepository;

	public Cep findByCodigoLogradouroAndIdLogradouro(Long lgrgeocod, Long lgrgeoeixnum) {
		return cepRepository.findByCodigoLogradouroAndIdLogradouro(lgrgeocod, lgrgeoeixnum);
	}

	public Cep save(Cep cep) {
		return cepRepository.save(cep);
	}
}
