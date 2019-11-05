package br.gov.sp.prodesp.ssp.dipol.enderecoservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.gov.sp.prodesp.ssp.dipol.enderecoservice.domain.entity.Cep;

public interface CepRepository extends JpaRepository<Cep, Long> {

	Cep findByCodigoLogradouroAndIdLogradouro(Long lgrgeocod, Long lgrgeoeixnum);

}
