package br.com.devinno.listedapi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tb_access")
public class Access {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cd_access")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "cd_user")
	private UserListed user;
	
	@ManyToOne
	@JoinColumn(name = "cd_category")
	private Category category;
	
	@ManyToOne
	@JoinColumn(name = "cd_project")
	private Project project;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UserListed getUser() {
		return user;
	}

	public void setUser(UserListed user) {
		this.user = user;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}
}
