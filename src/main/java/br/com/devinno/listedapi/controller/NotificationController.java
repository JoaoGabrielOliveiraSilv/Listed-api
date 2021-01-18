package br.com.devinno.listedapi.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.devinno.listedapi.service.NotificationService;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

	@Autowired
	private NotificationService service;

	@GetMapping
	public ResponseEntity<?> getNotifications(HttpServletRequest request, HttpServletResponse response) {
		return this.service.getNotificationsUser(request, response);
	}

	@PutMapping("/{id_notification}")
	public ResponseEntity<?> readNotification(@PathVariable(name = "id_notification") Long idNotification) {
		return this.service.readNotification(idNotification);
	}
	
	@DeleteMapping("/{id_notification}")
	public ResponseEntity<?> deleteNotification(HttpServletRequest request, @PathVariable("id_notification") Long idNotification) {
		return this.service.deleteNotification(request, idNotification);
	}
}
