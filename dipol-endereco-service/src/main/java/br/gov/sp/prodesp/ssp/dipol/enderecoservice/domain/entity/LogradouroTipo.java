package br.gov.sp.prodesp.ssp.dipol.enderecoservice.domain.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "lgrgeotip")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LogradouroTipo implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "lgrgeotipcod")
	private Long id;

	@Column(name = "lgrgeotipdes")
	private String descricao;

	@Column(name = "lgrgeogrpcod")
	private Long lgrgeogrpcod;

}
