package br.gov.sp.prodesp.ssp.dipol.enderecoservice.domain.vo;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

import io.swagger.annotations.ApiModel;

@Getter
@Setter
@ApiModel(value = "FiltroLatitudeLongitudeVO", description = "É a entidade responsável pelo filtro de logradouro, através da Latitude e Longitude")
public class FiltroLatitudeLongitudeVO {

	@NotNull(message = "Latitude é obrigatória")
	private BigDecimal latitude;

	@NotNull(message = "Longitude é obrigatório")
	private BigDecimal longitude;

}
