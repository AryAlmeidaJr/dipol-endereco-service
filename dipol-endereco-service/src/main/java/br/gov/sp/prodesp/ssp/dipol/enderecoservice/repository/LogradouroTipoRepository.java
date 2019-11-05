package br.gov.sp.prodesp.ssp.dipol.enderecoservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.gov.sp.prodesp.ssp.dipol.enderecoservice.domain.entity.LogradouroTipo;

public interface LogradouroTipoRepository extends JpaRepository<LogradouroTipo, Long> {

	LogradouroTipo findByDescricao(String tipo);

}