package br.gov.sp.prodesp.ssp.dipol.enderecoservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.util.GeometricShapeFactory;

import br.gov.sp.prodesp.ssp.dipol.enderecoservice.domain.entity.Delegacia;
import br.gov.sp.prodesp.ssp.dipol.enderecoservice.domain.vo.FiltroLatitudeLongitudeVO;
import br.gov.sp.prodesp.ssp.dipol.enderecoservice.repository.DelegaciaRepository;

@Service
public class DelegaciaService {

	@Autowired
	private DelegaciaRepository delegaciaRepository;

	public List<Delegacia> findByLatitudeAndLongitude(FiltroLatitudeLongitudeVO filtro) {
		return delegaciaRepository.findByLatitudeAndLongitude(createPoint(filtro.getLongitude().doubleValue(), filtro.getLatitude().doubleValue()));
	}

	/*
	 * Cria o Polygon (Circler) com o menor tamanho possivel, com o Latitude e Longitude informado. Para ser utilizado como
	 * um Point
	 */
	private Geometry createPoint(double longitude, double latitude) {
		GeometricShapeFactory shapeFactory = new GeometricShapeFactory();
		shapeFactory.setNumPoints(4);
		shapeFactory.setCentre(new Coordinate(longitude, latitude));
		shapeFactory.setSize(0);
		return shapeFactory.createCircle();
	}

}
