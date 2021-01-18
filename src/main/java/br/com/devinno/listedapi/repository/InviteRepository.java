package br.com.devinno.listedapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.devinno.listedapi.model.Access;
import br.com.devinno.listedapi.model.Invite;
import br.com.devinno.listedapi.model.UserListed;

@Repository
public interface InviteRepository extends JpaRepository<Invite, Long>{

	List<Invite> findByUser(UserListed user);
	
	List<Invite> findByAccess(Access access);
	
	void deleteAllByUser(UserListed user);
	
	void deleteAllByAccess(Access access);
}
