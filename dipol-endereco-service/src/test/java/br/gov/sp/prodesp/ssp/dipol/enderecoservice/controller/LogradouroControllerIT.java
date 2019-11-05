package br.gov.sp.prodesp.ssp.dipol.enderecoservice.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.gov.sp.prodesp.ssp.dipol.commons.domain.ErrorResponse;
import br.gov.sp.prodesp.ssp.dipol.commons.domain.FieldError;
import br.gov.sp.prodesp.ssp.dipol.commons.lang.CollectionUtils;
import br.gov.sp.prodesp.ssp.dipol.enderecoservice.domain.dto.LogradouroDTO;
import br.gov.sp.prodesp.ssp.dipol.enderecoservice.domain.entity.Logradouro;
import br.gov.sp.prodesp.ssp.dipol.enderecoservice.domain.entity.LogradouroTipo;
import br.gov.sp.prodesp.ssp.dipol.enderecoservice.repository.LogradouroRepository;
import br.gov.sp.prodesp.ssp.dipol.enderecoservice.repository.LogradouroTipoRepository;
import br.gov.sp.prodesp.ssp.dipol.enderecoservice.service.LogradouroService;

@DisplayName("Logradouro Controller")
public class LogradouroControllerIT extends BaseControllerIT {

	private static final String RESOURCE_BASE_URL = "/logradouros";
	private static final String FIND_BY_LATITUDE_LONGITUDE = "/findByLatitudeAndLongitude";

	@Autowired
	TestRestTemplate restTemplate;

	@Autowired
	LogradouroService logradouroService;

	@Autowired
	LogradouroRepository repositoryLogradouro;

	@Autowired
	LogradouroTipoRepository repositoryLogradouroTipo;

	@BeforeEach
	public void beforeEach() {
		createLogradoutoTipo();
		repositoryLogradouro.deleteAll();
	}

	@Nested
	@DisplayName("GET /logradouros")
	class Get {

		@Nested
		@DisplayName("deve retornar uma lista logradouros com status HTTP 200")
		class GetSuccess {

			@Test
			@DisplayName("quando todos os filtros são fornecidos")
			public void testGetWhenAllParameters() {
				List<LogradouroDTO> dto = createLogradouroDTO();

				StringBuilder url = new StringBuilder(RESOURCE_BASE_URL);
				url.append(String.format("?uf=%s", dto.get(0).getUf()));
				url.append(String.format("&municipio=%s", dto.get(0).getDescricaoMunicipio()));
				url.append(String.format("&search=%s", dto.get(0).getDescricaoLogradouro()));

				ParameterizedTypeReference<List<LogradouroDTO>> responseType = new ParameterizedTypeReference<List<LogradouroDTO>>() {};

				ResponseEntity<List<LogradouroDTO>> responseGet = restTemplate.exchange(url.toString(), HttpMethod.GET, getRequestEntity(), responseType);

				assertNotNull(responseGet);
				assertNotNull(responseGet.getBody());
				assertThat(responseGet.getStatusCode(), equalTo(HttpStatus.OK));
				assertThat(responseGet.getBody(), hasSize(1));
				assertEqualsToExample(responseGet.getBody().get(0), dto.get(0));
			}

			@Test
			@DisplayName("quando não existe na base e busca no google")
			public void testGetWhenFromGoogle() {
				createLogradouroDTO();
				createLogradoutoTipo();

				StringBuilder url = new StringBuilder(RESOURCE_BASE_URL);
				url.append(String.format("?uf=%s", "sp"));
				url.append(String.format("&municipio=%s", "vinhedo"));
				url.append(String.format("&search=%s", "rua osvaldo melli"));

				ParameterizedTypeReference<List<LogradouroDTO>> responseType = new ParameterizedTypeReference<List<LogradouroDTO>>() {};

				ResponseEntity<List<LogradouroDTO>> responseGet = restTemplate.exchange(url.toString(), HttpMethod.GET, getRequestEntity(), responseType);

				assertNotNull(responseGet);
				assertNotNull(responseGet.getBody());
				assertThat(responseGet.getStatusCode(), equalTo(HttpStatus.OK));
				assertEquals(true, CollectionUtils.isNotEmpty(responseGet.getBody()));
				assertEquals(true, responseGet.getBody().get(0).getFromGoogle());
				checkDTO(responseGet.getBody().get(0));
			}

			@Test
			@DisplayName("deve retornar uma lista vazia com status HTTP 200 quando nenhum endereço for encontrado")
			public void testGetWhenNoResult() {
				createLogradouroDTO();

				StringBuilder url = new StringBuilder(RESOURCE_BASE_URL);
				url.append(String.format("?uf=%s", "dd"));
				url.append(String.format("&municipio=%s", "sdad"));
				url.append(String.format("&search=%s", "asdas"));

				ParameterizedTypeReference<List<LogradouroDTO>> responseType = new ParameterizedTypeReference<List<LogradouroDTO>>() {};

				ResponseEntity<List<LogradouroDTO>> responseGet = restTemplate.exchange(url.toString(), HttpMethod.GET, getRequestEntity(), responseType);

				assertNotNull(responseGet);
				assertNotNull(responseGet.getBody());
				assertThat(responseGet.getStatusCode(), equalTo(HttpStatus.OK));
				assertEquals(0, responseGet.getBody().size());
			}
		}

		@Nested
		@DisplayName("deve retornar Status HTTP 400")
		class GetFailure {

			@Test
			@DisplayName("Quando apenas o parametro UF é informado")
			public void testGetWhenOnlyUfIsProvided() {
				StringBuilder url = new StringBuilder(RESOURCE_BASE_URL);
				url.append(String.format("?uf=%s", "sp"));
				url.append(String.format("&municipio=%s", ""));
				url.append(String.format("&search=%s", ""));

				ResponseEntity<ErrorResponse> response = restTemplate.exchange(url.toString(), HttpMethod.GET, getRequestEntity(), ErrorResponse.class);

				FieldError expectedMunicipio = new FieldError("municipio", "Municipio é obrigatório");
				FieldError expectedSearch = new FieldError("search", "Search é obrigatório");

				assertNotNull(response);
				assertNotNull(response.getBody());
				assertThat(response.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
				assertThat(response.getBody().getMessage(), equalTo("Bad Request"));
				assertThat(response.getBody().getTimestamp(), not(nullValue()));
				assertThat(response.getBody().getFieldErros(), hasSize(3));
				assertThat(response.getBody().getFieldErros(), hasItems(expectedMunicipio));
				assertThat(response.getBody().getFieldErros(), hasItems(expectedSearch));
			}

			@Test
			@DisplayName("Quando apenas o parametro Municipio é informado")
			public void testGetWhenOnlyMunicipioIsProvided() {
				StringBuilder url = new StringBuilder(RESOURCE_BASE_URL);
				url.append(String.format("?uf=%s", ""));

				url.append(String.format("&municipio=%s", "vinhedo"));
				url.append(String.format("&search=%s", ""));

				ResponseEntity<ErrorResponse> response = restTemplate.exchange(url.toString(), HttpMethod.GET, getRequestEntity(), ErrorResponse.class);

				FieldError expectedUf = new FieldError("uf", "UF é obrigatório");
				FieldError expectedSearch = new FieldError("search", "Search é obrigatório");

				assertNotNull(response);
				assertNotNull(response.getBody());
				assertThat(response.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
				assertThat(response.getBody().getMessage(), equalTo("Bad Request"));
				assertThat(response.getBody().getTimestamp(), not(nullValue()));
				assertThat(response.getBody().getFieldErros(), hasSize(4));
				assertThat(response.getBody().getFieldErros(), hasItems(expectedUf));
				assertThat(response.getBody().getFieldErros(), hasItems(expectedSearch));
			}

			@Test
			@DisplayName("Quando apenas o parametro Search é informado")
			public void testGetWhenOnlySearchIsProvided() {
				StringBuilder url = new StringBuilder(RESOURCE_BASE_URL);
				url.append(String.format("?uf=%s", ""));
				url.append(String.format("&municipio=%s", ""));
				url.append(String.format("&search=%s", "osvaldo"));

				ResponseEntity<ErrorResponse> response = restTemplate.exchange(url.toString(), HttpMethod.GET, getRequestEntity(), ErrorResponse.class);

				FieldError expectedUf = new FieldError("uf", "UF é obrigatório");
				FieldError expectedMunicipio = new FieldError("municipio", "Municipio é obrigatório");

				assertNotNull(response);
				assertNotNull(response.getBody());
				assertThat(response.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
				assertThat(response.getBody().getMessage(), equalTo("Bad Request"));
				assertThat(response.getBody().getTimestamp(), not(nullValue()));
				assertThat(response.getBody().getFieldErros(), hasSize(3));
				assertThat(response.getBody().getFieldErros(), hasItems(expectedUf));
				assertThat(response.getBody().getFieldErros(), hasItems(expectedMunicipio));
			}
		}
	}

	@Nested
	@DisplayName("GET /logradouros/findByLatitudeAndLongitude")
	class GetByLatitudeAndLongitude {

		@Test
		@DisplayName("quando todos os filtros são fornecidos")
		public void testGetWhenAllParameters() {
			StringBuilder url = new StringBuilder(RESOURCE_BASE_URL + FIND_BY_LATITUDE_LONGITUDE);
			url.append(String.format("?latitude=%s", new BigDecimal(-23.0141388)));
			url.append(String.format("&longitude=%s", new BigDecimal(-46.9686314)));

			ParameterizedTypeReference<List<LogradouroDTO>> responseType = new ParameterizedTypeReference<List<LogradouroDTO>>() {};

			ResponseEntity<List<LogradouroDTO>> responseGet = restTemplate.exchange(url.toString(), HttpMethod.GET, getRequestEntity(), responseType);

			assertNotNull(responseGet);
			assertNotNull(responseGet.getBody());
			assertThat(responseGet.getStatusCode(), equalTo(HttpStatus.OK));
		}

		@Test
		@DisplayName("Quando nenhum dos parametros é informado")
		public void testCreateWhenBadRequest() {
			StringBuilder url = new StringBuilder(RESOURCE_BASE_URL + FIND_BY_LATITUDE_LONGITUDE);

			ResponseEntity<ErrorResponse> response = restTemplate.exchange(url.toString(), HttpMethod.GET, getRequestEntity(new LogradouroDTO()), ErrorResponse.class);

			FieldError expectedLatitude = new FieldError("latitude", "Latitude é obrigatória");
			FieldError expectedLongitude = new FieldError("longitude", "Longitude é obrigatório");

			assertNotNull(response);
			assertNotNull(response.getBody());
			assertThat(response.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
			assertThat(response.getBody().getMessage(), equalTo("Bad Request"));
			assertThat(response.getBody().getDetails(), equalTo("uri=" + url.toString()));
			assertThat(response.getBody().getTimestamp(), not(nullValue()));
			assertThat(response.getBody().getFieldErros(), hasSize(2));
			assertThat(response.getBody().getFieldErros(), hasItems(expectedLatitude));
			assertThat(response.getBody().getFieldErros(), hasItems(expectedLongitude));
		}

		@Test
		@DisplayName("Quando apenas o campo Latitude é informado")
		public void testGetWhenOnlyLatitude() {
			StringBuilder url = new StringBuilder(RESOURCE_BASE_URL + FIND_BY_LATITUDE_LONGITUDE);
			url.append(String.format("?latitude=%s", new BigDecimal(-23.0141388)));
			url.append(String.format("&longitude=%s", ""));

			ResponseEntity<ErrorResponse> response = restTemplate.exchange(url.toString(), HttpMethod.GET, getRequestEntity(new LogradouroDTO()), ErrorResponse.class);

			FieldError expectedLongitude = new FieldError("longitude", "Longitude é obrigatório");

			assertNotNull(response);
			assertNotNull(response.getBody());
			assertThat(response.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
			assertThat(response.getBody().getMessage(), equalTo("Bad Request"));
			assertThat(response.getBody().getTimestamp(), not(nullValue()));
			assertThat(response.getBody().getFieldErros(), hasSize(1));
			assertThat(response.getBody().getFieldErros(), hasItems(expectedLongitude));
		}

		@Test
		@DisplayName("Quando apenas o campo Longitude é informado")
		public void testGetWhenOnlyLongitude() {
			StringBuilder url = new StringBuilder(RESOURCE_BASE_URL + FIND_BY_LATITUDE_LONGITUDE);
			url.append(String.format("?latitude=%s", ""));
			url.append(String.format("&longitude=%s", new BigDecimal(-46.9686314)));

			ResponseEntity<ErrorResponse> response = restTemplate.exchange(url.toString(), HttpMethod.GET, getRequestEntity(new LogradouroDTO()), ErrorResponse.class);

			FieldError expectedLatitude = new FieldError("latitude", "Latitude é obrigatória");

			assertNotNull(response);
			assertNotNull(response.getBody());
			assertThat(response.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
			assertThat(response.getBody().getMessage(), equalTo("Bad Request"));
			assertThat(response.getBody().getTimestamp(), not(nullValue()));
			assertThat(response.getBody().getFieldErros(), hasSize(1));
			assertThat(response.getBody().getFieldErros(), hasItems(expectedLatitude));
		}
	}

	@Nested
	@DisplayName("POST /logradouros")
	class Post {

		@Nested
		@DisplayName("deve retornar Status HTTP código 400")
		class PostFailure {

			@Test
			@DisplayName("Quando nenhum dos parametros é informado")
			public void testCreateWhenBadRequest() {
				StringBuilder url = new StringBuilder(RESOURCE_BASE_URL);

				ResponseEntity<ErrorResponse> response = restTemplate.exchange(url.toString(), HttpMethod.POST, getRequestEntity(new LogradouroDTO()), ErrorResponse.class);

				FieldError expectedDescricaoLogradouro = new FieldError("descricaoLogradouro", "Logradouro é obrigatório");
				FieldError expectedDescricaoBairro = new FieldError("descricaoBairro", "Bairro é obrigatório");
				FieldError expectedDescricaoMunicipio = new FieldError("descricaoMunicipio", "Municipio é obrigatório");
				FieldError expectedDescricaoCep = new FieldError("descricaoCep", "Cep é obrigatório");
				FieldError expectedUf = new FieldError("uf", "UF é obrigatório");
				FieldError expectedLat = new FieldError("latitude", "Latitude é obrigatório");
				FieldError expectedLng = new FieldError("longitude", "Longitude é obrigatório");

				assertNotNull(response);
				assertNotNull(response.getBody());
				assertThat(response.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
				assertThat(response.getBody().getMessage(), equalTo("Bad Request"));
				assertThat(response.getBody().getDetails(), equalTo("uri=" + url.toString()));
				assertThat(response.getBody().getTimestamp(), not(nullValue()));
				assertThat(response.getBody().getFieldErros(), hasSize(7));
				assertThat(response.getBody().getFieldErros(), hasItems(expectedDescricaoLogradouro));
				assertThat(response.getBody().getFieldErros(), hasItems(expectedDescricaoBairro));
				assertThat(response.getBody().getFieldErros(), hasItems(expectedDescricaoMunicipio));
				assertThat(response.getBody().getFieldErros(), hasItems(expectedDescricaoCep));
				assertThat(response.getBody().getFieldErros(), hasItems(expectedUf));
				assertThat(response.getBody().getFieldErros(), hasItems(expectedLat));
				assertThat(response.getBody().getFieldErros(), hasItems(expectedLng));
			}

		}

		@Test
		@DisplayName("Deve retornar um novo Logradouro com e HTTP com status 201")
		public void testCreate() {
			LogradouroDTO dto = builderLogradouroDTO().build();

			StringBuilder url = new StringBuilder(RESOURCE_BASE_URL);

			ResponseEntity<Long> response = restTemplate.exchange(url.toString(), HttpMethod.POST, getRequestEntity(dto), Long.class);

			assertNotNull(response);
			assertNotNull(response.getBody());
			assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));
			assertNotNull(response.getBody());
		}
	}

	public LogradouroDTO.LogradouroDTOBuilder builderLogradouroDTO() {
		return LogradouroDTO.builder()//
				.descricaoBairro("DNS")//
				.descricaoCep("12345678")//
				.descricaoLogradouro("DNS")//
				.descricaoMunicipio("DNS")//
				.fromGoogle(false)//
				.latitude(BigDecimal.valueOf(faker.number().randomDouble(10, 1, Integer.MAX_VALUE)))//
				.longitude(BigDecimal.valueOf(faker.number().randomDouble(10, 1, Integer.MAX_VALUE)))//
				.uf("SP");
	}

	public List<LogradouroDTO> createLogradouroDTO() {
		List<LogradouroDTO> listDto = new ArrayList<>();
		listDto.add(builderLogradouroDTO().descricaoLogradouro("AVENIDA DNS").build());
		listDto.add(builderLogradouroDTO().build());
		listDto.add(builderLogradouroDTO().build());

		Logradouro logradouro = null;

		for (LogradouroDTO dto : listDto) {
			logradouro = logradouroService.save(dto);
			assertNotNull(logradouro.getId());

			dto.setId(logradouro.getId());
			dto.setLogradouroFullName(dto.getDescricaoLogradouro() + " - " + dto.getDescricaoBairro() + ", " + dto.getDescricaoMunicipio() + " - " + dto.getUf());
		}

		return convertToDTO(listDto, LogradouroDTO.class);
	}

	public LogradouroTipo.LogradouroTipoBuilder builderLogradouroTipo() {
		return LogradouroTipo.builder().id(1L).descricao("Rua");
	}

	public List<LogradouroTipo> createLogradoutoTipo() {
		List<LogradouroTipo> listTipo = new ArrayList<>();
		listTipo.add(LogradouroTipo.builder().id(1L).descricao("AVENIDA").build());
		listTipo.add(LogradouroTipo.builder().id(2L).descricao("RUA").build());

		listTipo.forEach((tipo) -> {
			tipo = repositoryLogradouroTipo.save(tipo);
			assertNotNull(tipo.getId());
		});

		return listTipo;
	}

	void assertEqualsToExample(LogradouroDTO expected, LogradouroDTO actual) {
		assertEquals(expected.getDescricaoBairro(), actual.getDescricaoBairro());
		assertEquals(expected.getDescricaoCep(), actual.getDescricaoCep());
		assertEquals(expected.getDescricaoLogradouro(), actual.getDescricaoLogradouro());
		assertEquals(expected.getFromGoogle(), actual.getFromGoogle());
		assertEquals(expected.getUf(), actual.getUf());
	}

	void checkDTO(LogradouroDTO dto) {
		assertEquals(true, StringUtils.isNotBlank(dto.getDescricaoBairro()));
		assertEquals(true, StringUtils.isNotBlank(dto.getDescricaoCep()));
		assertEquals(true, StringUtils.isNotBlank(dto.getDescricaoLogradouro()));
		assertEquals(true, StringUtils.isNotBlank(dto.getUf()));
		assertEquals(true, dto.getFromGoogle() != null);
	}
}
