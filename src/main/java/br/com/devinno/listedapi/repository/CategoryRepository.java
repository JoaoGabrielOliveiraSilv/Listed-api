package br.com.devinno.listedapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.devinno.listedapi.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
	
	@Query("select c from Category c, UserListed u, Project p, Access a "
			+ "where p.id = :idProject "
				+ "and u.username = :userName "
					+ "and a.user = u "
						+ "and a.project = p "
							+ "and a.category = c")
	Optional<Category> findByUsernameAndProjectId(@Param("userName") String username, @Param("idProject") Long idProject);
}
