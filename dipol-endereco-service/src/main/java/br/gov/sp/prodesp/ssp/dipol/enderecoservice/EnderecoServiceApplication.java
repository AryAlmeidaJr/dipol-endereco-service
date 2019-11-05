package br.gov.sp.prodesp.ssp.dipol.enderecoservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;

import br.gov.sp.prodesp.ssp.dipol.commons.component.KeycloakWebSecurityConfiguration;
import br.gov.sp.prodesp.ssp.dipol.commons.exeption.handler.RestExceptionHandler;

@EnableDiscoveryClient
@SpringBootApplication
@Import({ RestExceptionHandler.class, KeycloakWebSecurityConfiguration.class })
public class EnderecoServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EnderecoServiceApplication.class, args);
	}

}
