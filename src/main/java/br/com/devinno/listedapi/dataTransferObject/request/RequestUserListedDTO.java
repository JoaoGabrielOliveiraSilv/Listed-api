package br.com.devinno.listedapi.dataTransferObject.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class RequestUserListedDTO {

	@Size(max = 50)
	@NotEmpty
	@NotNull
	private String name;

	@Size(max = 21)
	@NotEmpty
	@NotNull
	private String username;
	
	@Email
	@NotEmpty
	@NotNull
	private String email;
	
	@Size(min = 8)
	@NotEmpty
	@NotNull
	private String password;

	public RequestUserListedDTO(String name, String username, String email, String password) {
		this.name = name;
		this.username = username;
		this.email = email;
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
