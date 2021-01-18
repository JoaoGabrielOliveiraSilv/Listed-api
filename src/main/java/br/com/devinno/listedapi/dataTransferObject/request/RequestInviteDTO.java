package br.com.devinno.listedapi.dataTransferObject.request;

import javax.validation.constraints.Size;

public class RequestInviteDTO {

	@Size(max = 21)
	private String username;
	
	@Size(min = 2, max = 2)
	private String role;
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username.toLowerCase();
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
}
