package br.gov.sp.prodesp.ssp.dipol.enderecoservice.domain.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.vividsolutions.jts.geom.Geometry;

import io.swagger.annotations.ApiModel;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "LogradouroDTO", description = "É a entidade que identifica um logradouro.")
public class LogradouroDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

	private String logradouroFullName;

	@NotBlank(message = "Logradouro é obrigatório")
	private String descricaoLogradouro;

	private Long idBairro;

	@NotBlank(message = "Bairro é obrigatório")
	private String descricaoBairro;

	private Long idMunicipio;

	@NotBlank(message = "Municipio é obrigatório")
	private String descricaoMunicipio;

	@NotBlank(message = "UF é obrigatório")
	@Size(max = 2, min = 2, message = "UF deve ter 2 caracteres")
	private String uf;

	private Long idCep;

	@NotBlank(message = "Cep é obrigatório")
	private String descricaoCep;

	private Boolean fromGoogle;

	@NotNull(message = "Latitude é obrigatório")
	private BigDecimal latitude;

	@NotNull(message = "Longitude é obrigatório")
	private BigDecimal longitude;

	private String pais;

	public LogradouroDTO(String uf, Long idMunicipio, String descricaoMunicipio, Long idBairro, String descricaoBairro, String descricaoLogradouro, String descricaoTipo, String descricaoCep,
			Geometry coordenadas) {
		this.uf = uf;
		this.idMunicipio = idMunicipio;
		this.descricaoMunicipio = descricaoMunicipio;
		this.idBairro = idBairro;
		this.descricaoBairro = descricaoBairro;
		this.descricaoLogradouro = descricaoLogradouro;
		this.descricaoCep = descricaoCep;
		pais = "BRASIL";

		latitude = BigDecimal.valueOf(coordenadas.getCoordinate().y);
		longitude = BigDecimal.valueOf(coordenadas.getCoordinate().x);

	}

}
