package br.com.devinno.listedapi.dataTransferObject.request;

import javax.validation.constraints.Size;

public class RequestUpdatePasswordDTO {

	private String password;
	
	@Size(min = 8)
	private String newPassword;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
}
