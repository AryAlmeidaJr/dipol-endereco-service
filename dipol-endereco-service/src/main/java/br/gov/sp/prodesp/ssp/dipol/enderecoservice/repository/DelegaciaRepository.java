package br.gov.sp.prodesp.ssp.dipol.enderecoservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vividsolutions.jts.geom.Geometry;

import br.gov.sp.prodesp.ssp.dipol.enderecoservice.domain.entity.Delegacia;

public interface DelegaciaRepository extends JpaRepository<Delegacia, Long> {

	@Query(value = "Select d from Delegacia d where intersects(d.coordenadas, :area) = true")
	List<Delegacia> findByLatitudeAndLongitude(@Param("area") Geometry geometry);

}
