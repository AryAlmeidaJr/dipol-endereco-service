package br.gov.sp.prodesp.ssp.dipol.enderecoservice.repository;

import java.util.List;

import br.gov.sp.prodesp.ssp.dipol.enderecoservice.domain.dto.LogradouroDTO;
import br.gov.sp.prodesp.ssp.dipol.enderecoservice.domain.vo.FiltroLogradouroVO;

public interface LogradouroRepositoryCustom {

	List<LogradouroDTO> findByLogradouro(FiltroLogradouroVO filtro);

}
