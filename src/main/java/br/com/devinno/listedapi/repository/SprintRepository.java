package br.com.devinno.listedapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.devinno.listedapi.model.Sprint;

@Repository
public interface SprintRepository extends JpaRepository<Sprint, Long> {

	@Query("select s from Sprint s, Project p "
			+ "where s.project = p "
				+ "and p.id = :idProject "
					+ "order by s.dateStart desc, s.id desc")
	List<Sprint> findByProjectId(@Param("idProject") Long idProject);
}
