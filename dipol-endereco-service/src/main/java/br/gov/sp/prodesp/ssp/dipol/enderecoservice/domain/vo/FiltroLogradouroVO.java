package br.gov.sp.prodesp.ssp.dipol.enderecoservice.domain.vo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;

@Getter
@Setter
@ApiModel(value = "FiltroLogradouroVO", description = "É a entidade responsável pelo filtro de logradouro")
public class FiltroLogradouroVO {

	@NotBlank(message = "UF é obrigatório")
	@Size(max = 2, min = 2, message = "UF deve ter 2 caracteres")
	private String uf;

	@NotBlank(message = "Municipio é obrigatório")
	private String municipio;

	@NotBlank(message = "Search é obrigatório")
	@Size(min = 3, message = "Search deve ter no mínimo 3 caracteres")
	private String search;

	@ApiParam(value = "Indica se é para realizar busca no Google")
	private Boolean buscaGoogle;
}
