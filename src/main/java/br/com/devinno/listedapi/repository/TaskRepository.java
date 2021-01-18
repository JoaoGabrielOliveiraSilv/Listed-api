package br.com.devinno.listedapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.devinno.listedapi.model.Access;
import br.com.devinno.listedapi.model.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

	@Query("select t from Task t, Backlog b "
			+ "where b.id = :idBacklog "
				+ "and t.backlog = b "
					+ "order by t.backlog asc, t.name asc")
	List<Task> findByBacklogId(@Param("idBacklog") Long idBacklog);
	
	List<Task> findByAssigned(Access access);
}
