package br.com.devinno.listedapi.dataTransferObject.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

public class RequestUpdateUserListedDTO {

	@Size(max = 50)
	private String name;
	
	@Size(max = 21)
	private String username;
	
	@Email
	@Size(max = 120)
	private String email;
	
	@Size(max = 255)
	private String biography;
	
	@Size(max = 20)
	private String tag;
	
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

	public String getBiography() {
		return biography;
	}

	public void setBiography(String biography) {
		this.biography = biography;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String Tag) {
		this.tag = Tag;
	}
}
