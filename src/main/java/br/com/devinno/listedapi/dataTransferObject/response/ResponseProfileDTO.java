package br.com.devinno.listedapi.dataTransferObject.response;

import br.com.devinno.listedapi.model.UserListed;

public class ResponseProfileDTO {
	
	private String name;
	
	private String image;
	
	private String username;
	
	private String tag;
	
	private String biography;
	
	public ResponseProfileDTO(UserListed user) {
		this.name = user.getName();
		this.image = user.getImage();
		this.username = user.getUsername();
		this.tag = user.getTag();
		this.biography = user.getBiography();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getTag() {
		return tag;
	}

	public void setEmail(String email) {
		this.tag = email;
	}

	public String getBiography() {
		return biography;
	}

	public void setBiography(String biography) {
		this.biography = biography;
	}
}
