package br.gov.sp.prodesp.ssp.dipol.enderecoservice.container;

import lombok.Getter;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.shaded.org.apache.commons.lang.StringUtils;

import br.gov.sp.prodesp.ssp.dipol.test.container.DefaultPostgresContainer;

public class PostgreGISContainer extends PostgreSQLContainer<DefaultPostgresContainer> {

	@Getter
	private static boolean enabled = Boolean.parseBoolean(StringUtils.defaultIfBlank(System.getProperties().getProperty("dipol.testcontainer.enabled"), "true"));

	@Getter
	private static String imageVersion = StringUtils.defaultIfBlank(System.getProperties().getProperty("dipol.testcontainer.postgis.image"), "mdillon/postgis:10");

	private static PostgreGISContainer container;

	public static PostgreGISContainer getInstance() {
		if (container == null) {
			container = new PostgreGISContainer();
			container.withStartupTimeoutSeconds(600);
		}

		return container;
	}

	private PostgreGISContainer() {
		super(getImageVersion());
	}

	@Override
	public void start() {
		super.start();
		System.setProperty("DATABASE_URL", container.getJdbcUrl());
		System.setProperty("DATABASE_NAME", container.getDatabaseName());
		System.setProperty("DATABASE_USERNAME", container.getUsername());
		System.setProperty("DATABASE_PASSWORD", container.getPassword());
		System.setProperty("DATABASE_JPA_DDL_AUTO", "create-drop");
		System.setProperty("DATABASE_JPA_GENERATE_DDL", "true");
		System.setProperty("DATABASE_JPA_SHOW_SQL", "true");
	}

	@Override
	public void stop() {
		//do nothing, JVM handles shut down
	}

}
