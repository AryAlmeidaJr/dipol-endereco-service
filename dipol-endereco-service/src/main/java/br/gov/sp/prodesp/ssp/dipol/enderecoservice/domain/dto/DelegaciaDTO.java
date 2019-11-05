package br.gov.sp.prodesp.ssp.dipol.enderecoservice.domain.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import io.swagger.annotations.ApiModel;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "DelegaciaDTO", description = "É a entidade que identifica uma delegacia.")
public class DelegaciaDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

	private String descricao;

	private Integer codigo;

}
