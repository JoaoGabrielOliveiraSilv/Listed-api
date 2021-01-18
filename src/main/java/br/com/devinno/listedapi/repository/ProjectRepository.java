package br.com.devinno.listedapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.devinno.listedapi.model.Project;
import br.com.devinno.listedapi.model.UserListed;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

	@Query("select p from Project p, Access a, UserListed u "
			+ "where u = :user "
				+ "and a.user = u "
					+ "and a.project = p "
						+ "order by p.id desc")
	List<Project> findByUser(@Param("user") UserListed user);
	
	@Query("select u from Category c, Project p, Access a, UserListed u"
			+ " where p.id = :idProject"
				+ " and a.project = p"
					+ " and a.user = u"
						+ " and c.id = 1"
							+ " and a.category = c ")
	Optional<UserListed> findProductOwnerByProjectId(@Param("idProject") Long idProject);
	
	@Query("select u from Category c, Project p, Access a, UserListed u"
			+ " where p.id = :idProject"
				+ " and a.project = p"
					+ " and a.user = u"
						+ " and c.id = 2"
							+ " and a.category = c ")
	Optional<UserListed> findScrumMasterByProjectId(@Param("idProject") Long idProject);
	
	@Query("select u from Category c, Project p, Access a, UserListed u"
			+ " where p.name = :idProject"
				+ " and a.project = p"
					+ " and a.user = u"
						+ " and c.id = 3"
							+ " and a.category = c "
								+ "order by u.id desc")
	List<UserListed> findDeveloperTeamByProjectId(@Param("idProject") Long idProject);
	
	@Query("select u from Project p, Access a, UserListed u"
			+ " where p.id = :idProject"
				+ " and a.project = p"
					+ " and a.user = u")
	List<UserListed> findTeamByProjectId(@Param("idProject") Long idProject);
}
