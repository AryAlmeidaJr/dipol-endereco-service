package br.gov.sp.prodesp.ssp.dipol.enderecoservice.domain.entity;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.vividsolutions.jts.geom.Geometry;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Entity
@Table(name = "BAIGEO")
public class Bairro implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "OBJECTID")
	private Long objectid;

	@Id
	@Column(name = "BAIGEOCOD")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "baigeo_baigeocod_seq")
	@SequenceGenerator(name = "baigeo_baigeocod_seq", sequenceName = "baigeo_baigeocod_seq", allocationSize = 1)
	private Long id;

	@Column(name = "BAIGEONOM")
	private String nome;

	@Column(name = "GLOBALID")
	private String globalid;

	@JsonBackReference
	@Column(name = "SHAPE")
	private Geometry coordenadas;

	@Column(name = "CREATED_USER")
	private String createdUser;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE")
	private Calendar createdDate;

	@Column(name = "LAST_EDITED_USER")
	private String lastEditedUser;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LAST_EDITED_DATE")
	private Calendar lastEditedDate;

}
