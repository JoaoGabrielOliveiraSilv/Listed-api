package br.com.devinno.listedapi.errorMessage;

import org.springframework.http.HttpStatus;

public class ErrorResponse {
	
	private String title;
	
	private String message;
	
	private HttpStatus status;
	
	public ErrorResponse() {
		
	}
	
	public ErrorResponse(String title, String message, HttpStatus status) {
		this.title = title;
		this.message = message;
		this.status = status;
	}
	
	public ErrorResponse(HttpStatus status, String entity) {
		
		if(status == HttpStatus.NOT_FOUND) {
			if(entity.equalsIgnoreCase("project")) {
				this.title = "Projeto não encontrado";
				this.message = "Este projeto não existe";
			}else if(entity.equalsIgnoreCase("user")) {
				this.title = "Usuário não encontrado";
				this.message = "Este usuário não existe";
			}else if(entity.equalsIgnoreCase("category")) {
				this.title = "Categoria não encontrada";
				this.message = "Esta categoria não existe";
			}else if(entity.equalsIgnoreCase("invite")) {
				this.title = "Convite não encontrado";
				this.message = "Este convite não existe";
			}else if(entity.equalsIgnoreCase("notification")) {
				this.title = "Notificação não encontrada";
				this.message = "Esta notificação não existe";
			}else if(entity.equalsIgnoreCase("backlog")) {
				this.title = "Backlog não encontrado";
				this.message = "Este backlog não existe";
			}else if(entity.equalsIgnoreCase("userToken")) {
				this.title = "Usuário de token não encontrado";
				this.message = "O usuário inserido na codificação do token não existe";
			}else if(entity.equalsIgnoreCase("task")) {
				this.title = "Tarefa não encontrada";
				this.message = "Esta tarefa não existe";
			}else if(entity.equalsIgnoreCase("sprint")) {
				this.title = "Sprint não encontrada";
				this.message = "Esta sprint não existe";
			}else if(entity.equalsIgnoreCase("sprintBacklog")) {
				this.title = "Sprint backlog não encontrado";
				this.message = "Este backlog não faz parte dessa Sprint";
			}else {
				this.title = "Recurso não encontrado";
				this.message = "o recurso requisitado não está presente em nosso banco!";
			}
			this.status = HttpStatus.NOT_FOUND;
		}
		if(status == HttpStatus.FORBIDDEN) {
			this.title = "Sem permissão";
			this.message = "Você não tem permissão para acessar está funcionalidade";
			this.status = status;
		}
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public HttpStatus getStatus() {
		return status;
	}
	
	public void setStatus(HttpStatus status) {
		this.status = status;
	}
	
	public String toJson() {
		return "{\"title\":\"" + this.title +"\"," +
				"\"message\":\"" + this.message +"\"," +
				"\"status\":\"" + this.status +"\"}"
				;
	}
}
