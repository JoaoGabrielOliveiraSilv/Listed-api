package br.com.devinno.listedapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.devinno.listedapi.model.SprintBacklog;

@Repository
public interface SprintBacklogRepository extends JpaRepository<SprintBacklog, Long>{

	@Query("select sb from SprintBacklog sb, Sprint s, Backlog b "
			+ "where s.id = :idSprint "
				+ "and b.id = :idBacklog "
					+ "and sb.sprint = s "
						+ "and sb.backlog = b "
							+ "order by s.dateStart desc, s.id desc")
	Optional<SprintBacklog> findBySprintIdAndBacklogId(@Param("idSprint") Long idSprint, @Param("idBacklog") Long idBacklog);
	
	@Query("select sb from SprintBacklog sb, Sprint s "
			+ "where s.id = :idSprint "
				+ "and sb.sprint = s "
					+ "order by s.dateStart desc, s.id desc")
	List<SprintBacklog> findBySprintId(@Param("idSprint") Long idSprint);
	
	@Query("select sb from SprintBacklog sb, Sprint s "
			+ "where s.id = :idSprint "
				+ "and sb.sprint = s "
					+ "and sb.concluded = :concluded ")
	Optional<SprintBacklog> findBySprintIdAndConcluded(@Param("idSprint") Long idSprint, @Param("concluded") boolean concluded);
	
	@Query("select sb from SprintBacklog sb, Backlog b "
			+ "where b.id = :idBacklog "
				+ "and sb.backlog = b "
					+ "order by sb.id desc")
	List<SprintBacklog> findByBacklogId(@Param("idBacklog") Long idBacklog);
	
	@Query("select sb from SprintBacklog sb, Backlog b "
			+ "where b.id = :idBacklog "
				+ "and sb.backlog = b "
					+ "and sb.concluded = :concluded")
	Optional<SprintBacklog> findByBacklogIdAndConcluded(@Param("idBacklog") Long idBacklog, @Param("concluded") boolean concluded);
}
