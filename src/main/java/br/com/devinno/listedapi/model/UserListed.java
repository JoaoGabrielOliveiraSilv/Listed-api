package br.com.devinno.listedapi.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import br.com.devinno.listedapi.dataTransferObject.request.RequestUpdateUserListedDTO;
import br.com.devinno.listedapi.dataTransferObject.request.RequestUserListedDTO;

@Entity
@Table(name = "tb_user")
public class UserListed {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cd_user")
	private Long id;
	
	@Size(max = 150)
	@Column(name = "im_user")
	private String image;
	
	@Size(max = 50)
	@NotBlank
	@Column(name = "nm_user")
	private String name;
	
	@Size(max = 21)
	@NotBlank
	@Column(name = "nm_username", unique = true)
	private String username;
	
	@Size(max = 120)
	@Email
	@NotBlank
	@Column(name = "nm_email", unique = true)
	private String email;
	
	@Size(max = 255, min = 8)
	@NotBlank
	@Column(name = "nm_password")
	private String password;
	
	@Size(max = 20)
	@Column(name = "nm_tag")
	private String tag;
	
	@Size(max = 255)
	@Column(name = "ds_biography")
	private String biography;
	
	@OneToMany(mappedBy = "user")
	private List<Access> access;

	public UserListed() {
		
	}
	
	public UserListed(RequestUserListedDTO newUser) {
		this.name = newUser.getName();
		this.username = newUser.getUsername();
		this.email = newUser.getEmail();
		this.password = newUser.getPassword();
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getBiography() {
		return biography;
	}

	public void setBiography(String biography) {
		this.biography = biography;
	}
	
	public List<Access> getAccess() {
		return access;
	}

	public void setAccess(List<Access> access) {
		this.access = access;
	}

	public void populate(RequestUpdateUserListedDTO newUser) {
		//Checks which data will be updated
		if(newUser.getName() != null && !newUser.getName().equals(" "))
			this.name = newUser.getName();
		if(newUser.getUsername() != null && !newUser.getUsername().equals(" ")) {
			if(!newUser.getUsername().startsWith("@"))
				this.username = "@" + newUser.getUsername().toLowerCase();
			else
				this.username = newUser.getUsername().toLowerCase();
		}
		if(newUser.getEmail() != null && !newUser.getEmail().equals(" "))
			this.email = newUser.getEmail().toLowerCase();
		if(newUser.getBiography() != null)
			this.biography = newUser.getBiography();
		if(newUser.getTag() != null)
			this.tag = newUser.getTag();
	}
	
}
