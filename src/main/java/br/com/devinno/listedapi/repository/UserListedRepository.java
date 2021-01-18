package br.com.devinno.listedapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.devinno.listedapi.model.UserListed;

@Repository
public interface UserListedRepository extends JpaRepository<UserListed, Long> {

	Optional<UserListed> findByUsername(String username);
	Optional<UserListed> findByEmail(String email);
	
	// Implementa o metódo que busca usuários através do seu username e email 
	@Query("select u from UserListed u "
			+ "where u.username = :username "
				+ "or u.email = :email")
	Optional<UserListed> findByUsernameOrEmail(@Param("username") String username, @Param("email") String email);
}
