package br.gov.sp.prodesp.ssp.dipol.enderecoservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.gov.sp.prodesp.ssp.dipol.enderecoservice.domain.entity.Bairro;

public interface BairroRepository extends JpaRepository<Bairro, Long> {

	Bairro findByNome(String bairro);

}
