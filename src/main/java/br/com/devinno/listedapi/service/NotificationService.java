package br.com.devinno.listedapi.service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.devinno.listedapi.JWT.JWTTools;
import br.com.devinno.listedapi.dataTransferObject.response.ResponseNotificationDTO;
import br.com.devinno.listedapi.errorMessage.ErrorResponse;
import br.com.devinno.listedapi.handler.ResponseHandler;
import br.com.devinno.listedapi.model.Notification;
import br.com.devinno.listedapi.model.UserListed;
import br.com.devinno.listedapi.repository.NotificationRepository;

@Service
public class NotificationService {

	@Autowired
	private NotificationRepository repository;

	@Autowired
	private JWTTools tokenTools;

	private JSONObject message = new JSONObject();

	public ResponseEntity<?> getNotificationsUser(HttpServletRequest request, HttpServletResponse response){
		Optional<UserListed> user = tokenTools.findUserByToken(request);

		if(!user.isPresent()) {
			ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND, "userToken");
			return ResponseHandler.toResponseEntity(error, HttpStatus.NOT_FOUND);
		}

		List<Notification> notifications = repository.findAllByUser(user.get());

		if(notifications.isEmpty()) {
			return ResponseHandler.toResponseEntity(notifications, HttpStatus.OK);
		}

		List<ResponseNotificationDTO> notificationsResponse = new ArrayList<ResponseNotificationDTO>();

		for(Notification notification: notifications) {
			notificationsResponse.add(new ResponseNotificationDTO(notification));
		}

		return ResponseHandler.toResponseEntity(notificationsResponse, HttpStatus.OK);
	}

	public ResponseEntity<?> readNotification(Long id) {
		Optional<Notification> notification = repository.findById(id);

		if(!notification.isPresent()) {
			ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND, "notification");

			return ResponseHandler.toResponseEntity(error, HttpStatus.NOT_FOUND);
		}
		if(!notification.get().isRead()) {
			notification.get().setRead(true);		
			repository.save(notification.get());

			message.put("message", "Notificação visualizada");
			return ResponseHandler.toResponseEntity(message.toMap(), HttpStatus.OK);
		} 
		ErrorResponse error = new ErrorResponse();

		error.setTitle("Notificação já visualizada");
		error.setMessage("Está notificação já foi visualizada");
		error.setStatus(HttpStatus.CONFLICT);

		return ResponseHandler.toResponseEntity(error, HttpStatus.CONFLICT);
	}
	
	public ResponseEntity<?> deleteNotification(HttpServletRequest request, Long idNotification) {
		Optional<Notification> notification = repository.findById(idNotification);

		if(!notification.isPresent()) {
			ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND, "notification");

			return ResponseHandler.toResponseEntity(error, HttpStatus.NOT_FOUND);
		}
		
		this.repository.delete(notification.get());
		
		this.message.put("message", "Notificação deletada com sucesso");
		return ResponseHandler.toResponseEntity(message.toMap(), HttpStatus.OK);
	}
}
