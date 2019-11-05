package br.gov.sp.prodesp.ssp.dipol.enderecoservice.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.runner.RunWith;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import br.gov.sp.prodesp.ssp.dipol.enderecoservice.EnderecoServiceApplication;
import br.gov.sp.prodesp.ssp.dipol.enderecoservice.container.PostgreGISContainer;
import br.gov.sp.prodesp.ssp.dipol.test.controller.AbstractControllerIT;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { EnderecoServiceApplication.class }, webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public abstract class BaseControllerIT extends AbstractControllerIT {

	protected static AccessTokenResponse accessToken;

	static {
		if (PostgreGISContainer.isEnabled()) {
			PostgreGISContainer.getInstance().start();
		}
	}

	@BeforeAll
	static void beforeAll() throws Exception {
		accessToken = login();
	}

	protected HttpEntity<?> getRequestEntity() {
		return new HttpEntity<>(getHeaders(accessToken.getToken()));
	}

	protected <T> org.springframework.http.HttpEntity<T> getRequestEntity(T object) {
		return new org.springframework.http.HttpEntity<>(object, getHeaders(accessToken.getToken()));
	}

}
