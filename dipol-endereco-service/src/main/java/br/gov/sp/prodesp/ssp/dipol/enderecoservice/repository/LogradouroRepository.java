package br.gov.sp.prodesp.ssp.dipol.enderecoservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.gov.sp.prodesp.ssp.dipol.enderecoservice.domain.entity.Logradouro;

public interface LogradouroRepository extends JpaRepository<Logradouro, Long>, LogradouroRepositoryCustom {
	
}