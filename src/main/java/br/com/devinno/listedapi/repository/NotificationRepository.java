package br.com.devinno.listedapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.devinno.listedapi.model.Notification;
import br.com.devinno.listedapi.model.UserListed;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long>{

	List<Notification> findAllByUser(UserListed user);
	
	void deleteAllByUser(UserListed user);
}
