package br.com.devinno.listedapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.devinno.listedapi.model.Backlog;

@Repository
public interface BacklogRepository extends JpaRepository<Backlog, Long> {

	@Query("select b from Backlog b, Project p "
			+ "where p.id = :idProject "
				+ "and b.project = p "
					+ "order by b.priority ASC, b.difficulty ASC")
	List<Backlog> findByProjectId(@Param("idProject") Long idProject);
	
}
