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

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vividsolutions.jts.geom.Geometry;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Entity
@Table(name = "MUNARE")
public class Municipio implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "MUNCOD")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "munare_muncod_seq")
	@SequenceGenerator(name = "munare_muncod_seq", sequenceName = "munare_muncod_seq", allocationSize = 1)
	private Long id;

	@Column(name = "MUNNOM")
	private String nome;

	@Column(name = "MUNHABNUM")
	private Long munhabnum;

	@Column(name = "UFCOD")
	private String uf;

	@JsonBackReference
	@JsonIgnore
	@Column(name = "MUNGEOAREGEOG")
	private Geometry coordenadas;

}
