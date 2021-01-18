package br.com.devinno.listedapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.devinno.listedapi.model.Access;
import br.com.devinno.listedapi.model.Project;
import br.com.devinno.listedapi.model.UserListed;

@Repository
public interface AccessRepository extends JpaRepository<Access, Long>{
	
	List<Access> findByUser(UserListed user);
	
	List<Access> findByProject(Project project);
	
	Optional<Access> findByUserAndProject(UserListed user, Project project);
	
	void deleteAllByUser(UserListed user);
}
