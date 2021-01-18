package br.com.devinno.listedapi.dataTransferObject.response;

import java.time.LocalDate;

import br.com.devinno.listedapi.model.Notification;

public class ResponseNotificationDTO {
	
	private Long id;
	
	private String name;
	
	private String description;
	
	private LocalDate date;
	
	private String type;
	
	private boolean read;

	public ResponseNotificationDTO(Notification notification) {
		this.id = notification.getId();
		this.name = notification.getName();
		this.description = notification.getDescription();
		this.date = notification.getDate();
		this.type = notification.getType();
		this.read = notification.isRead();
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}
}
