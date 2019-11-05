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
@Table(name = "DPGEO")
public class Delegacia implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "DPGEOCOD")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dpgeo_dpgeocod_seq")
	@SequenceGenerator(name = "dpgeo_dpgeocod_seq", sequenceName = "dpgeo_dpgeocod_seq", allocationSize = 1)
	private Long id;

	@Column(name = "DPGEODES")
	private String descricao;

	@Column(name = "dpgeocod", insertable = false, updatable = false)
	private Integer codigo;

	@JsonBackReference
	@JsonIgnore
	@Column(name = "SHAPE")
	private Geometry coordenadas;

}
