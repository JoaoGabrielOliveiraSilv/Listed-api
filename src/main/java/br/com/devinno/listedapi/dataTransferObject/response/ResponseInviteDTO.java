package br.com.devinno.listedapi.dataTransferObject.response;

import java.time.LocalDate;

import br.com.devinno.listedapi.model.Invite;

public class ResponseInviteDTO {

	private Long id;
	
	private String name;
	
	private String description;
	
	private LocalDate date;
	
	private String status;
	
	private String role;
	
	private String senderUsername;
	
	private String projectName;
	
	public ResponseInviteDTO(Invite invite) {
		this.setId(invite.getId());
		this.name = invite.getName();
		this.description = invite.getDescription();
		this.date = invite.getDate();
		this.status = invite.getStatus();
		this.role = invite.getRole();
		this.senderUsername = invite.getAccess().getUser().getUsername();
		this.projectName = invite.getAccess().getProject().getName();
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getSenderUsername() {
		return senderUsername;
	}

	public void setSenderUsername(String sender) {
		this.senderUsername = sender;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
}
