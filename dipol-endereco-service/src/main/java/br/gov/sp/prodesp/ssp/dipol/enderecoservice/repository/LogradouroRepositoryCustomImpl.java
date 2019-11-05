package br.gov.sp.prodesp.ssp.dipol.enderecoservice.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;

import br.gov.sp.prodesp.ssp.dipol.enderecoservice.domain.dto.LogradouroDTO;
import br.gov.sp.prodesp.ssp.dipol.enderecoservice.domain.entity.Bairro;
import br.gov.sp.prodesp.ssp.dipol.enderecoservice.domain.entity.Bairro_;
import br.gov.sp.prodesp.ssp.dipol.enderecoservice.domain.entity.Cep;
import br.gov.sp.prodesp.ssp.dipol.enderecoservice.domain.entity.Cep_;
import br.gov.sp.prodesp.ssp.dipol.enderecoservice.domain.entity.Logradouro;
import br.gov.sp.prodesp.ssp.dipol.enderecoservice.domain.entity.LogradouroTipo;
import br.gov.sp.prodesp.ssp.dipol.enderecoservice.domain.entity.LogradouroTipo_;
import br.gov.sp.prodesp.ssp.dipol.enderecoservice.domain.entity.Logradouro_;
import br.gov.sp.prodesp.ssp.dipol.enderecoservice.domain.entity.Municipio;
import br.gov.sp.prodesp.ssp.dipol.enderecoservice.domain.entity.Municipio_;
import br.gov.sp.prodesp.ssp.dipol.enderecoservice.domain.vo.FiltroLogradouroVO;

public class LogradouroRepositoryCustomImpl implements LogradouroRepositoryCustom {

	@Autowired
	private EntityManager entityManager;

	@Override
	public List<LogradouroDTO> findByLogradouro(FiltroLogradouroVO filtro) {
		CriteriaBuilder cBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<LogradouroDTO> cQuery = cBuilder.createQuery(LogradouroDTO.class);

		Root<Logradouro> rootLogradouro = cQuery.from(Logradouro.class);
		Root<Cep> rootCep = cQuery.from(Cep.class);

		Join<Logradouro, Municipio> leftJoinMunicipio = rootLogradouro.join(Logradouro_.municipio, JoinType.LEFT);
		Join<Logradouro, Bairro> leftJoinBairro = rootLogradouro.join(Logradouro_.bairro, JoinType.LEFT);
		Join<Logradouro, LogradouroTipo> leftJoinTipo = rootLogradouro.join(Logradouro_.tipo, JoinType.LEFT);

		Predicate ufName = cBuilder.equal(leftJoinMunicipio.get(Municipio_.uf), filtro.getUf().toUpperCase());
		Predicate municipioName = cBuilder.equal(leftJoinMunicipio.get(Municipio_.nome), filtro.getMunicipio().toUpperCase());

		Predicate joinLogradouroCep = cBuilder.equal(rootLogradouro.get(Logradouro_.codigo), rootCep.get(Cep_.codigoLogradouro));

		// substitui os espaços em branco ' ', por '%'. Assim amplio o campo de busca.
		String search = filtro.getSearch().replace(" ", "%");
		Predicate logradouroName = cBuilder.like(rootLogradouro.get(Logradouro_.nomeCompleto), "%" + search.toUpperCase() + "%");
		Predicate cepNumero = cBuilder.like(rootCep.get(Cep_.codigoCep), "%" + search + "%");

		Predicate logradouroOrCep = cBuilder.or(logradouroName, cepNumero);

		cQuery.multiselect(leftJoinMunicipio.get(Municipio_.uf), leftJoinMunicipio.get(Municipio_.id), leftJoinMunicipio.get(Municipio_.nome), leftJoinBairro.get(Bairro_.id),
				leftJoinBairro.get(Bairro_.nome), rootLogradouro.get(Logradouro_.nomeCompleto), leftJoinTipo.get(LogradouroTipo_.descricao), rootCep.get(Cep_.codigoCep),
				rootLogradouro.get(Logradouro_.coordenadas));

		cQuery.where(ufName, municipioName, joinLogradouroCep, logradouroOrCep);

		cQuery.groupBy(leftJoinMunicipio.get(Municipio_.uf), leftJoinMunicipio.get(Municipio_.id), leftJoinMunicipio.get(Municipio_.nome), leftJoinBairro.get(Bairro_.id),
				leftJoinBairro.get(Bairro_.nome), rootLogradouro.get(Logradouro_.nomeCompleto), leftJoinTipo.get(LogradouroTipo_.descricao), rootCep.get(Cep_.codigoCep),
				rootLogradouro.get(Logradouro_.coordenadas));

		TypedQuery<LogradouroDTO> query = entityManager.createQuery(cQuery);

		List<LogradouroDTO> list = query.getResultList();

		for (LogradouroDTO dto : list) {
			dto.setFromGoogle(false);
			dto.setLogradouroFullName(dto.getDescricaoLogradouro() + " - " + dto.getDescricaoBairro() + ", " + dto.getDescricaoMunicipio() + " - " + dto.getUf());
		}

		return list;

	}
}
