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
@Table(name = "tb_notification")
public class Notification {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cd_notification")
	private Long id;
	
	@NotEmpty
	@Size(max = 60)
	@Column(name = "nm_notification")
	private String name;
	
	@NotEmpty
	@Size(max = 255)
	@Column(name = "ds_notification")
	private String description;

	@Column(name = "dt_notification")
	private LocalDate date;
	
	@Size(max = 3)
	@Column(name = "sg_type_notification")
	private String type;
	
	@Column(name = "id_read")
	private boolean read;
	
	@ManyToOne
	@JoinColumn(name = "cd_user")
	private UserListed user;

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}

	public UserListed getUser() {
		return user;
	}

	public void setUser(UserListed user) {
		this.user = user;
	}
}
