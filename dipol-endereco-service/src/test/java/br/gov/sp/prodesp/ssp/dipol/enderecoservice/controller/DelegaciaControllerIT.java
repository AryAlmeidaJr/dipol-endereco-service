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

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.util.GeometricShapeFactory;

import br.gov.sp.prodesp.ssp.dipol.commons.domain.ErrorResponse;
import br.gov.sp.prodesp.ssp.dipol.commons.domain.FieldError;
import br.gov.sp.prodesp.ssp.dipol.enderecoservice.domain.dto.DelegaciaDTO;
import br.gov.sp.prodesp.ssp.dipol.enderecoservice.domain.dto.LogradouroDTO;
import br.gov.sp.prodesp.ssp.dipol.enderecoservice.domain.entity.Delegacia;
import br.gov.sp.prodesp.ssp.dipol.enderecoservice.repository.DelegaciaRepository;

@DisplayName("Delegacia Controller")
public class DelegaciaControllerIT extends BaseControllerIT {

	private static final String RESOURCE_BASE_URL = "/delegacias";
	private static final String FIND_BY_LATITUDE_LONGITUDE = "/findByLatitudeAndLongitude";

	@Autowired
	TestRestTemplate restTemplate;

	@Autowired
	DelegaciaRepository delegaciaRepository;

	@BeforeEach
	public void beforeEach() {
		delegaciaRepository.deleteAll();
	}

	@Nested
	@DisplayName("GET /delegacias")
	class Get {

		//		@Test
		@DisplayName("quando todos os filtros são fornecidos")
		public void testGetWhenAllParameters() {
			List<DelegaciaDTO> dto = createDelegacia();

			StringBuilder url = new StringBuilder(RESOURCE_BASE_URL + FIND_BY_LATITUDE_LONGITUDE);
			url.append(String.format("?latitude=%s", new BigDecimal(-23.026755)));
			url.append(String.format("&longitude=%s", new BigDecimal(-46.981683)));

			ParameterizedTypeReference<List<DelegaciaDTO>> responseType = new ParameterizedTypeReference<List<DelegaciaDTO>>() {};

			ResponseEntity<List<DelegaciaDTO>> responseGet = restTemplate.exchange(url.toString(), HttpMethod.GET, getRequestEntity(), responseType);

			assertNotNull(responseGet);
			assertNotNull(responseGet.getBody());
			assertThat(responseGet.getStatusCode(), equalTo(HttpStatus.OK));
			assertThat(responseGet.getBody(), hasSize(1));
			assertEqualsToExample(responseGet.getBody().get(0), dto.get(0));
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
			url.append(String.format("?latitude=%s", new BigDecimal(-23.026755)));
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
			url.append(String.format("&longitude=%s", new BigDecimal(-46.981683)));

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

	private Geometry createPoint(double longitude, double latitude) {
		GeometricShapeFactory shapeFactory = new GeometricShapeFactory();
		shapeFactory.setNumPoints(10);
		shapeFactory.setCentre(new Coordinate(longitude, latitude));
		shapeFactory.setSize(0.01);
		return shapeFactory.createCircle();
	}

	public List<DelegaciaDTO> createDelegacia() {
		List<Delegacia> delegacias = new ArrayList<>();
		delegacias.add(Delegacia.builder().descricao("vinhedo").coordenadas(createPoint(-46.981683, -23.026755)).build());
		delegacias.add(Delegacia.builder().descricao("são paulo").coordenadas(createPoint(-46.636758, -23.552072)).build());
		delegacias.add(Delegacia.builder().descricao("santos").coordenadas(createPoint(-46.329080, -23.968601)).build());

		for (Delegacia delegacia : delegacias) {
			delegacia = delegaciaRepository.save(delegacia);
			assertNotNull(delegacia.getId());
		}

		return convertToDTO(delegacias, DelegaciaDTO.class);
	}

	void assertEqualsToExample(DelegaciaDTO expected, DelegaciaDTO actual) {
		assertEquals(expected.getId(), actual.getId());
		assertEquals(expected.getDescricao(), actual.getDescricao());
	}

}
