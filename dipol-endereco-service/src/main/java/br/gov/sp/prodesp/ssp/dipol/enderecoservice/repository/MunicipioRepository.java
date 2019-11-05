package br.gov.sp.prodesp.ssp.dipol.enderecoservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.gov.sp.prodesp.ssp.dipol.enderecoservice.domain.entity.Municipio;

public interface MunicipioRepository extends JpaRepository<Municipio, Long> {

	Municipio findByUfAndNome(String uf, String municipio);
}
