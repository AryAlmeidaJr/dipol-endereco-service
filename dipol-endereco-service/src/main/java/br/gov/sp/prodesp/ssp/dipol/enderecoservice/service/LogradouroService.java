package br.gov.sp.prodesp.ssp.dipol.enderecoservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.util.GeometricShapeFactory;

import br.gov.sp.prodesp.ssp.dipol.enderecoservice.domain.dto.LogradouroDTO;
import br.gov.sp.prodesp.ssp.dipol.enderecoservice.domain.entity.Bairro;
import br.gov.sp.prodesp.ssp.dipol.enderecoservice.domain.entity.Cep;
import br.gov.sp.prodesp.ssp.dipol.enderecoservice.domain.entity.Logradouro;
import br.gov.sp.prodesp.ssp.dipol.enderecoservice.domain.entity.LogradouroTipo;
import br.gov.sp.prodesp.ssp.dipol.enderecoservice.domain.entity.Municipio;
import br.gov.sp.prodesp.ssp.dipol.enderecoservice.domain.vo.FiltroLogradouroVO;
import br.gov.sp.prodesp.ssp.dipol.enderecoservice.repository.LogradouroRepository;

@Service
public class LogradouroService {

	private static final String USER_API_GOOGLE = "API_GOOGLE";

	private static final String LOGRADOURO_TIPO_RUA = "RUA";
	private static final String LOGRADOURO_TIPO_AVENIDA = "AVENIDA";

	private static final Long LONG_0 = 0L;
	private static final Long LONG_999 = 999L;
	private static final Integer NUMERO_PONTOS_32 = 32;

	@Autowired
	private LogradouroRepository logradouroRepository;

	@Autowired
	private CepService cepService;

	@Autowired
	private MunicipioService municipioService;

	@Autowired
	private BairroService bairroService;

	@Autowired
	private LogradouroTipoService logradouroTipoService;

	public List<LogradouroDTO> findByLogradouro(FiltroLogradouroVO filtro) {
		List<LogradouroDTO> resultados = logradouroRepository.findByLogradouro(filtro);
		List<LogradouroDTO> logradouros = new ArrayList<>();

		// necessário eliminar os logradouros duplicados na mão, por conta do groupBy por Coordenadas
		for (LogradouroDTO resultado : resultados) {
			String novoLogradouro = resultado.getLogradouroFullName();
			Optional<LogradouroDTO> optional = logradouros.stream().filter(item -> item.getLogradouroFullName().equals(novoLogradouro)).findFirst();

			if (!optional.isPresent()) {
				logradouros.add(resultado);
			}
		}

		return logradouros;
	}

	public Logradouro save(Logradouro logradouro) {
		return logradouroRepository.save(logradouro);
	}

	public Logradouro save(LogradouroDTO dto) {

		String descricaoLogadouro = dto.getDescricaoLogradouro();

		boolean isAvenida = descricaoLogadouro.toUpperCase().contains(LOGRADOURO_TIPO_AVENIDA);

		// identifica se o tipo de Logradouro é "AVENIDA", caso não seja "AVENIDA" o default esta tipo "RUA"
		LogradouroTipo logradouroTipo = logradouroTipoService.findByDescricao(isAvenida ? LOGRADOURO_TIPO_AVENIDA : LOGRADOURO_TIPO_RUA); //fixo AVENIDA

		Municipio municipio = getMunicipio(dto.getUf(), dto.getDescricaoMunicipio());
		Bairro bairro = getBairro(dto.getDescricaoBairro());

		Logradouro logradouro = Logradouro.builder() //
				.objectid(LONG_999) //
				.codigo(LONG_0) // grava zerado, para posteriormente poder igualar ao ID Lgrgeoeixnum, não identifique um motivo para ser diferente, necessário para usar no CEP
				.idTipo(logradouroTipo.getId()) //
				.tipo(logradouroTipo) //
				.nome(descricaoLogadouro.toUpperCase()) // setar nome
				.nomeCompleto(descricaoLogadouro.toUpperCase()) //
				.numminesq(LONG_999) //
				.nummaxesq(LONG_999) //
				.nummindir(LONG_999) //
				.nummaxdir(LONG_999) //
				.reggeocod(LONG_999) //
				.idMunicipio(municipio != null ? municipio.getId() : null) // buscar municipio
				.municipio(municipio) //
				.idBairro(bairro != null ? bairro.getId() : null) // buscar bairro
				.bairro(bairro) //
				.lgrgeohrqcod(LONG_999) //
				.lgrgeodircod(LONG_999) //
				.lgrgeojurcod(LONG_999) //
				.lgrgeopavcod(LONG_999) //
				.lgrgeoavg(LONG_999) //
				.lgrgeovelmax(LONG_999) //
				.globalid(USER_API_GOOGLE) //
				.coordenadas(createCircle(dto.getLongitude().doubleValue(), dto.getLatitude().doubleValue(), 0.0005)) //
				.createdUser(USER_API_GOOGLE).build(); //

		logradouro = save(logradouro);

		logradouro.setCodigo(logradouro.getId()); // igualei com o Id pois não identifiquei um motivo para ter outro valor, necessário para buscar o CEP
		logradouro = save(logradouro); // necessário atualizar o campo Lgrgeocod igualado ao Id do Logradouro, pois é utilizado no CEP

		getCep(dto.getDescricaoCep(), logradouro);

		return logradouro;
	}

	/*
	 * Cria o Polygon (Circler) para o Lat Long informado
	 */
	private Geometry createCircle(double lng, double lat, double radius) {
		GeometricShapeFactory shapeFactory = new GeometricShapeFactory();
		shapeFactory.setNumPoints(NUMERO_PONTOS_32);
		shapeFactory.setCentre(new Coordinate(lng, lat));
		shapeFactory.setSize(radius);
		return shapeFactory.createCircle();
	}

	/*
	 * recupera o bairro a partir do bairro informado
	 */
	private Bairro getBairro(Object objBairro) {
		Bairro bairro = null;

		if (objBairro != null) {
			String nomeBairro = (String) objBairro;
			bairro = bairroService.findByNome(nomeBairro.toUpperCase());

			// salva um novo municipio na base caso não encontre um correspondente
			if (bairro == null) {
				bairro = saveNewBairro(nomeBairro);
			}
		}

		return bairro;
	}

	/*
	 * recupera o Cep a partir do cep informado
	 */
	private Cep getCep(Object objCep, Logradouro logradouro) {
		Cep cep = null;

		if (objCep != null) {
			String numeroCep = (String) objCep;
			cep = cepService.findByCodigoLogradouroAndIdLogradouro(logradouro.getCodigo(), logradouro.getId());

			// salva um novo municipio na base caso não encontre um correspondente
			if (cep == null) {
				cep = saveNewCep(numeroCep, logradouro);
			}
		}

		return cep;
	}

	/*
	 * recupera o municipio a partir do UF e Municipio informado
	 */
	private Municipio getMunicipio(Object objUf, Object objMunicipio) {
		Municipio municipio = null;

		if (objUf != null && objMunicipio != null) {

			String uf = (String) objUf;
			String nomeMunicipio = (String) objMunicipio;

			municipio = municipioService.findByUfAndNome(uf.toUpperCase(), nomeMunicipio.toUpperCase());

			// salva um novo municipio na base caso não encontre um correspondente
			if (municipio == null) {
				municipio = saveNewMunicipio(uf, nomeMunicipio);
			}
		}

		return municipio;
	}

	/*
	 * Salva um novo bairro na base
	 */
	private Bairro saveNewBairro(String nomeBairro) {
		Bairro bairro = Bairro.builder().objectid(LONG_999) //
				.nome(nomeBairro.toUpperCase()) //
				.globalid(USER_API_GOOGLE) //
				.createdUser(USER_API_GOOGLE) //
				.build();
		return bairroService.save(bairro);
	}

	/*
	 * salva novo CEP
	 */
	private Cep saveNewCep(String numeroCep, Logradouro logradouro) {
		Cep cep = Cep.builder() //
				.globalid(USER_API_GOOGLE) //
				.codigoCep(numeroCep.replace("-", "")) //
				.codigoLogradouro(logradouro.getCodigo()) //
				.idLogradouro(logradouro.getId()) //
				.build();
		return cepService.save(cep);
	}

	/*
	 * Salva um novo Municipio na base, para o UF informado
	 */
	private Municipio saveNewMunicipio(String uf, String nomeMunicipio) {
		Municipio municipio = Municipio.builder() //
				.uf(uf.toUpperCase()) //
				.nome(nomeMunicipio.toUpperCase()) //
				.build(); //

		return municipioService.save(municipio);
	}

}
