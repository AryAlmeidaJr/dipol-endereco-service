package br.gov.sp.prodesp.ssp.dipol.enderecoservice.domain.enumeration;

import lombok.Getter;

@Getter
public enum GoogleTypeEnum {

	TYPE_LOGRADOURO("route", "long_name"),
	TYPE_UF("administrative_area_level_1", "short_name"),
	TYPE_MUNICIPIO("administrative_area_level_2", "short_name"),
	TYPE_CEP("postal_code", "short_name"),
	TYPE_BAIRRO("sublocality_level_1", "long_name"),
	TYPE_PAIS("country", "long_name");

	private String type;
	private String size;

	private GoogleTypeEnum(String type, String size) {
		this.type = type;
		this.size = size;
	}

}