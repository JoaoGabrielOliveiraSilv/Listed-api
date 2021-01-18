package br.com.devinno.listedapi.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
@Table(name = "tb_invite")
public class Invite {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cd_invite")
	private Long id;
	
	@NotEmpty
	@Size(max = 80)
	@Column(name = "nm_invite")
	private String name;
	
	@NotEmpty
	@Size(max = 255)
	@Column(name = "ds_invite")
	private String description;

	@Column(name = "dt_invite")
	private LocalDate date;
	
	@Size(max = 8)
	@Column(name = "nm_status")
	private String status;
	
	@Size(max = 13)
	@Column(name = "nm_role")
	private String role;
	
	@ManyToOne
	@JoinColumn(name = "cd_user_invite")
	private UserListed user;
	
	@ManyToOne
	@JoinColumn(name = "cd_access_sender")
	private Access access;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public UserListed getUser() {
		return user;
	}

	public void setUser(UserListed user) {
		this.user = user;
	}

	public Access getAccess() {
		return access;
	}

	public void setAccess(Access access) {
		this.access = access;
	}
}
