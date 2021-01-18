package br.com.devinno.listedapi.dataTransferObject.response;

import br.com.devinno.listedapi.model.Access;
import br.com.devinno.listedapi.model.Category;
import br.com.devinno.listedapi.model.Project;
import br.com.devinno.listedapi.model.UserListed;

public class ResponseAccessDTO {

	private String user;
	
	private String username;
	
	private String image;
	
	private String role;
	
	public ResponseAccessDTO() {
		
	}

	public ResponseAccessDTO(Access access) {
		this.user = access.getUser().getName();
		this.username = access.getUser().getUsername();
		this.image = access.getUser().getImage();
		this.role = access.getCategory().getName();
	}
	
	public ResponseAccessDTO(UserListed user, Category category, Project project) {
		this.user = user.getName();
		this.username = user.getUsername();
		this.image = user.getImage();
		this.role = category.getName();
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
	

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String category) {
		this.role = category;
	}
}
