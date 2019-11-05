package br.gov.sp.prodesp.ssp.dipol.enderecoservice.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import br.gov.sp.prodesp.ssp.dipol.commons.controller.AbstractController;
import br.gov.sp.prodesp.ssp.dipol.enderecoservice.domain.dto.DelegaciaDTO;
import br.gov.sp.prodesp.ssp.dipol.enderecoservice.domain.vo.FiltroLatitudeLongitudeVO;
import br.gov.sp.prodesp.ssp.dipol.enderecoservice.service.DelegaciaService;

@RestController
@RequestMapping(value = "/delegacias")
@Api(value = "Conjunto de endpoints para recuperação de delegacias.", tags = { "Delegacia" })
public class DelegaciaController extends AbstractController {

	@Autowired
	private DelegaciaService delegaciaService;

	@GetMapping(value = "/findByLatitudeAndLongitude")
	@ApiOperation(value = "Retorna todas as delegacias responsáveis pela Latitude e Longitude informada", response = DelegaciaDTO.class)
	public List<DelegaciaDTO> findByLatitudeAndLongitude(@Valid FiltroLatitudeLongitudeVO filtro) {
		return convertToDTO(delegaciaService.findByLatitudeAndLongitude(filtro), DelegaciaDTO.class);
	}

}
