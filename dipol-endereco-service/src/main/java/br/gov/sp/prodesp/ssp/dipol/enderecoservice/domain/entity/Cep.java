package br.gov.sp.prodesp.ssp.dipol.enderecoservice.domain.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Entity
@Table(name = "LGRGEOCEP")
public class Cep implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "OBJECTID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lgrgeocep_objectid_seq")
	@SequenceGenerator(name = "lgrgeocep_objectid_seq", sequenceName = "lgrgeocep_objectid_seq", allocationSize = 1)
	private Long id;

	@Column(name = "LGRGEOCOD")
	private Long codigoLogradouro;

	@Column(name = "LGRGEOCEP")
	private String codigoCep;

	@Column(name = "LGRGEOEIXNUM")
	private Long idLogradouro;

	@Column(name = "GLOBALID")
	private String globalid;

}
