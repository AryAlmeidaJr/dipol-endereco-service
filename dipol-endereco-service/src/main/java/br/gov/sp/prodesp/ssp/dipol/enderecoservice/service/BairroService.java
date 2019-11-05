package br.gov.sp.prodesp.ssp.dipol.enderecoservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.sp.prodesp.ssp.dipol.enderecoservice.domain.entity.Bairro;
import br.gov.sp.prodesp.ssp.dipol.enderecoservice.repository.BairroRepository;

@Service
public class BairroService {

	@Autowired
	private BairroRepository bairroRepository;

	public Bairro findByNome(String bairro) {
		return bairroRepository.findByNome(bairro);
	}

	public Bairro save(Bairro bairro) {
		return bairroRepository.save(bairro);
	}

}
