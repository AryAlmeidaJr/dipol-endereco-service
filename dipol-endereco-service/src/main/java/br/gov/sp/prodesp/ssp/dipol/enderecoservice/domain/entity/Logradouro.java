package br.gov.sp.prodesp.ssp.dipol.enderecoservice.domain.entity;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vividsolutions.jts.geom.Geometry;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Entity
@Table(name = "LGRGEO")
public class Logradouro implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "OBJECTID")
	private Long objectid;

	@Id
	@Column(name = "LGRGEOEIXNUM")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lgrgeo_lgrgeoeixnum_seq")
	@SequenceGenerator(name = "lgrgeo_lgrgeoeixnum_seq", sequenceName = "lgrgeo_lgrgeoeixnum_seq", allocationSize = 1)
	private Long id;

	@Column(name = "LGRGEOCOD")
	private Long codigo;

	@Column(name = "LGRGEOTIPCOD")
	private Long idTipo;

	@ManyToOne(fetch = FetchType.EAGER, optional = true)
	@JoinColumn(name = "LGRGEOTIPCOD", referencedColumnName = "LGRGEOTIPCOD", insertable = false, updatable = false)
	private LogradouroTipo tipo;

	@Column(name = "LGRGEONOM")
	private String nome;

	@Column(name = "NUMMINESQ")
	private Long numminesq;

	@Column(name = "NUMMAXESQ")
	private Long nummaxesq;

	@Column(name = "NUMMINDIR")
	private Long nummindir;

	@Column(name = "NUMMAXDIR")
	private Long nummaxdir;

	@Column(name = "REGGEOCOD")
	private Long reggeocod;

	@Column(name = "MUNCOD")
	private Long idMunicipio;

	@ManyToOne(fetch = FetchType.EAGER, optional = true)
	@JoinColumn(name = "MUNCOD", referencedColumnName = "MUNCOD", insertable = false, updatable = false)
	private Municipio municipio;

	@Column(name = "BAIGEOCOD")
	private Long idBairro;

	@ManyToOne(fetch = FetchType.EAGER, optional = true)
	@JoinColumn(name = "BAIGEOCOD", referencedColumnName = "BAIGEOCOD", insertable = false, updatable = false)
	private Bairro bairro;

	@Column(name = "LGRGEOHRQCOD")
	private Long lgrgeohrqcod;

	@Column(name = "LGRGEODIRCOD")
	private Long lgrgeodircod;

	@Column(name = "LGRGEOJURCOD")
	private Long lgrgeojurcod;

	@Column(name = "LGRGEOPAVCOD")
	private Long lgrgeopavcod;

	@Column(name = "LGRGEOAVG")
	private Long lgrgeoavg;

	@Column(name = "LGRGEOVELMAX")
	private Long lgrgeovelmax;

	@Column(name = "GLOBALID")
	private String globalid;

	@JsonBackReference
	@JsonIgnore
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

	@Column(name = "LGRGEONOMFON")
	private String lgrgeonomfon;

	@Column(name = "MUNCODRDO")
	private Long muncodrdo;

	@Column(name = "LGRGEONOMCOMPL")
	private String nomeCompleto;

}
