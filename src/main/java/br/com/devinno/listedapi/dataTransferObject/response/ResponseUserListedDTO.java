package br.com.devinno.listedapi.dataTransferObject.response;

import java.util.ArrayList;
import java.util.List;

import br.com.devinno.listedapi.model.Access;
import br.com.devinno.listedapi.model.UserListed;

public class ResponseUserListedDTO {
		
	private String name;
	
	private String username;
	
	private String email;
	
	private String biography;
	
	private String tag;
	
	private List<ResponseAccessDTO> access = new ArrayList<ResponseAccessDTO>();
	
	private String image;
	
	public ResponseUserListedDTO() {
		
	}
	
	public ResponseUserListedDTO(UserListed newUser) {
		if(newUser.getUsername() != null)
			this.username = newUser.getUsername();
		if(newUser.getName() != null)
			this.name = newUser.getName();
		if(newUser.getEmail() != null)
			this.email = newUser.getEmail();
		if(newUser.getBiography() != null)
			this.biography = newUser.getBiography();
		if(newUser.getAccess() != null) {
			for(Access item: newUser.getAccess())
				this.access.add(new ResponseAccessDTO(item));
		}
		if(newUser.getTag() != null) 
			this.tag = newUser.getTag();
		if(newUser.getImage() != null)
			this.setImage(newUser.getImage());
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

	public String getBiography() {
		return biography;
	}

	public void setBiography(String biography) {
		this.biography = biography;
	}

	public String getTag() {
		return tag;
	}

	public List<ResponseAccessDTO> getAccess() {
		return access;
	}
	
	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	
	public void setAccess(List<ResponseAccessDTO> access) {
		this.access = access;
	}
}
