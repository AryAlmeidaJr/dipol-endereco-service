package br.gov.sp.prodesp.ssp.dipol.enderecoservice.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.AddressComponent;
import com.google.maps.model.AddressComponentType;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;

import br.gov.sp.prodesp.ssp.dipol.enderecoservice.domain.dto.LogradouroDTO;
import br.gov.sp.prodesp.ssp.dipol.enderecoservice.domain.enumeration.GoogleTypeEnum;
import br.gov.sp.prodesp.ssp.dipol.enderecoservice.domain.vo.FiltroLatitudeLongitudeVO;
import br.gov.sp.prodesp.ssp.dipol.enderecoservice.domain.vo.FiltroLogradouroVO;
import br.gov.sp.prodesp.ssp.dipol.enderecoservice.util.StringUtils;

@Service
public class GoogleService {

	@Value("${app.google.api-key}")
	private String apiKey;

	private static final String LONG_NAME = "long_name";

	public List<LogradouroDTO> findLogradouroByFiltro(FiltroLogradouroVO filtro) throws ApiException, InterruptedException, IOException {
		List<LogradouroDTO> listDto = new ArrayList<>();

		// concatena os parametros, para recuperar os endereços correspondetes apenas ao UF e Municipio informado
		String fullSearch = filtro.getSearch() + ", " + filtro.getMunicipio() + " " + filtro.getUf();

		GeoApiContext context = getGeoApiContext();
		GeocodingResult[] results = googleFindLogradouro(context, fullSearch);

		if (ArrayUtils.isNotEmpty(results)) {

			LogradouroDTO dto;

			for (GeocodingResult r : results) {

				dto = convertGoogleResultToLogradouroDTO(r);

				if (br.gov.sp.prodesp.ssp.dipol.enderecoservice.util.StringUtils.areNotBlanks(dto.getDescricaoBairro(), dto.getDescricaoCep(), dto.getDescricaoLogradouro(),
						dto.getDescricaoMunicipio())) {
					listDto.add(dto);
				}
			}
		}

		return listDto;
	}

	public List<LogradouroDTO> findLogradouroByLatitudeAndLongitude(FiltroLatitudeLongitudeVO filtro) throws ApiException, InterruptedException, IOException {
		List<LogradouroDTO> listDto = new ArrayList<>();

		GeoApiContext context = getGeoApiContext();
		GeocodingResult[] results = googleFindByLatitudeAndLongitude(context, filtro.getLatitude(), filtro.getLongitude());

		if (ArrayUtils.isNotEmpty(results)) {

			LogradouroDTO dto;

			for (GeocodingResult r : results) {

				dto = convertGoogleResultToLogradouroDTO(r);

				if (br.gov.sp.prodesp.ssp.dipol.enderecoservice.util.StringUtils.areNotBlanks(dto.getDescricaoBairro(), dto.getDescricaoCep(), dto.getDescricaoLogradouro(),
						dto.getDescricaoMunicipio())) {

					String novoLogradouro = dto.getLogradouroFullName();
					Optional<LogradouroDTO> optional = listDto.stream().filter(item -> item.getLogradouroFullName().equals(novoLogradouro)).findFirst();

					if (!optional.isPresent()) {
						listDto.add(dto);
					}
				}
			}
		}

		return listDto;
	}

	/*
	 * Gera conexão com RestServices do Google através da chave
	 */
	public GeoApiContext getGeoApiContext() {
		return new GeoApiContext.Builder().apiKey(apiKey).build();
	}

	/*
	 * Busca endereço no googleMaps utilizando o Logradouro informado
	 */
	public GeocodingResult[] googleFindLogradouro(GeoApiContext context, String logradouro) throws ApiException, InterruptedException, IOException {
		return GeocodingApi.geocode(context, logradouro).await();
	}

	/*
	 * Busca endereço no googleMaps, utilizando a latitude e longitude informados
	 */
	private GeocodingResult[] googleFindByLatitudeAndLongitude(GeoApiContext context, BigDecimal latitude, BigDecimal longitude) throws ApiException, InterruptedException, IOException {
		LatLng location = new LatLng();
		location.lat = latitude.doubleValue();
		location.lng = longitude.doubleValue();
		return GeocodingApi.reverseGeocode(context, location).await();
	}

	/*
	 * Método responsável por converter resultado da consulta do Google(GeocodingResult) para LogradouroDTO
	 */
	public LogradouroDTO convertGoogleResultToLogradouroDTO(GeocodingResult gr) {
		LogradouroDTO dto = new LogradouroDTO();

		dto.setDescricaoBairro(convertObjectToString(googleGetValueByType(gr.addressComponents, GoogleTypeEnum.TYPE_BAIRRO)));
		dto.setDescricaoCep(convertObjectToString(googleGetValueByType(gr.addressComponents, GoogleTypeEnum.TYPE_CEP)));
		dto.setDescricaoLogradouro(convertObjectToString(googleGetValueByType(gr.addressComponents, GoogleTypeEnum.TYPE_LOGRADOURO)));
		dto.setDescricaoMunicipio(convertObjectToString(googleGetValueByType(gr.addressComponents, GoogleTypeEnum.TYPE_MUNICIPIO)));
		dto.setLatitude(BigDecimal.valueOf(gr.geometry.location.lat));
		dto.setLongitude(BigDecimal.valueOf(gr.geometry.location.lng));
		dto.setFromGoogle(true);
		dto.setUf(convertObjectToString(googleGetValueByType(gr.addressComponents, GoogleTypeEnum.TYPE_UF)));
		dto.setPais(convertObjectToString(googleGetValueByType(gr.addressComponents, GoogleTypeEnum.TYPE_PAIS)));
		dto.setLogradouroFullName(dto.getDescricaoLogradouro() + " - " + dto.getDescricaoBairro() + ", " + dto.getDescricaoMunicipio() + " - " + dto.getUf());

		if (br.gov.sp.prodesp.ssp.dipol.enderecoservice.util.StringUtils.areNotBlanks(dto.getPais())) {
			dto.setLogradouroFullName(dto.getLogradouroFullName() + " - " + dto.getPais());
		}

		return dto;
	}

	/*
	 * recupera os valores do Objeto do AdrressComponent vindo do Google, a partir do tipo informado
	 */
	public Object googleGetValueByType(AddressComponent[] array, GoogleTypeEnum type) {
		Object object = null;

		for (AddressComponent c : array) {
			Optional<AddressComponentType> optional = Arrays.stream(c.types).filter(t -> t.toString().equals(type.getType())).findFirst();
			if (optional.isPresent()) {
				object = LONG_NAME.equals(type.getSize()) ? c.longName : c.shortName;
			}
		}

		return object;
	}

	/*
	 * método responsável por converter um Object para String
	 */
	private String convertObjectToString(Object o) {
		return o != null ? (String) o : StringUtils.EMPTY;
	}

}
