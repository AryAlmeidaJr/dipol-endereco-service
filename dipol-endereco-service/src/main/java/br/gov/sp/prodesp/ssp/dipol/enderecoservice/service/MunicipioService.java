package br.gov.sp.prodesp.ssp.dipol.enderecoservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.sp.prodesp.ssp.dipol.enderecoservice.domain.entity.Municipio;
import br.gov.sp.prodesp.ssp.dipol.enderecoservice.repository.MunicipioRepository;

@Service
public class MunicipioService {

	@Autowired
	private MunicipioRepository municipioRepository;

	public Municipio findByUfAndNome(String uf, String municipio) {
		return municipioRepository.findByUfAndNome(uf, municipio);
	}

	public Municipio save(Municipio municipio) {
		return municipioRepository.save(municipio);
	}

}
