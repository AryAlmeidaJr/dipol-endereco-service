package br.gov.sp.prodesp.ssp.dipol.enderecoservice.controller;

import java.io.IOException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.google.maps.errors.ApiException;

import io.swagger.annotations.ApiOperation;

import br.gov.sp.prodesp.ssp.dipol.commons.controller.AbstractController;
import br.gov.sp.prodesp.ssp.dipol.commons.lang.CollectionUtils;
import br.gov.sp.prodesp.ssp.dipol.enderecoservice.domain.dto.LogradouroDTO;
import br.gov.sp.prodesp.ssp.dipol.enderecoservice.domain.vo.FiltroLatitudeLongitudeVO;
import br.gov.sp.prodesp.ssp.dipol.enderecoservice.domain.vo.FiltroLogradouroVO;
import br.gov.sp.prodesp.ssp.dipol.enderecoservice.service.GoogleService;
import br.gov.sp.prodesp.ssp.dipol.enderecoservice.service.LogradouroService;

@RestController
@RequestMapping(value = "/logradouros")
@Validated
public class LogradouroController extends AbstractController {

	@Autowired
	private LogradouroService logradouroService;

	@Autowired
	private GoogleService googleService;

	/*
	 * metodo que busca em nossa base o logradouro a partir do serch informado, no caso a consulta é feita por LIKE
	 * '%serch%' em nossa base de Logradouro, caso não encontre nenhum endereço correspondente, realiza a consulta na API do
	 * Google(GoogleMaps)
	 */
	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Responsável por recuperar um logradouro, primeiramente em nossa base e caso necessário realiza a consulta no Google", response = LogradouroDTO.class)
	public List<LogradouroDTO> findByFiltro(@Valid FiltroLogradouroVO filtro) throws ApiException, InterruptedException, IOException {

		List<LogradouroDTO> listDto = logradouroService.findByLogradouro(filtro);

		if (CollectionUtils.isEmpty(listDto) && filtro.getBuscaGoogle()) {
			listDto = googleService.findLogradouroByFiltro(filtro);
		}

		return convertToDTO(listDto, LogradouroDTO.class);
	}

	@GetMapping(value = "findByLatitudeAndLongitude")
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Responsável por recuperar um logradouro no Google, a partir da Latitude e longitude informada", response = LogradouroDTO.class)
	public List<LogradouroDTO> findByLatitudeAndLongitude(@Valid FiltroLatitudeLongitudeVO filtro) throws ApiException, InterruptedException, IOException {
		return convertToDTO(googleService.findLogradouroByLatitudeAndLongitude(filtro), LogradouroDTO.class);
	}

	/*
	 * Método responsável por gravar um novo endereço na base a partir do LogradouroDTO enviado por parametro
	 */
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation(value = "Responsável por adicionar um novo Logradouro na base", response = Long.class)
	public Long save(@RequestBody @Valid LogradouroDTO dto) {
		return logradouroService.save(dto).getId();
	}

}
