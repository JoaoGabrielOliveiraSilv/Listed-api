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
@Table(name = "sprint_backlog")
public class SprintBacklog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cd_sprint_backlog")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "cd_backlog")
	private Backlog backlog;
	
	@ManyToOne
	@JoinColumn(name = "cd_sprint")
	private Sprint sprint;
	
	@Column(name = "id_concluded")
	private boolean concluded;

	public SprintBacklog() {
		
	}
	
	public SprintBacklog(Sprint sprint, Backlog backlog) {
		this.backlog = backlog;
		this.sprint = sprint;
		this.concluded = false;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Backlog getBacklog() {
		return backlog;
	}

	public void setBacklog(Backlog backlog) {
		this.backlog = backlog;
	}

	public Sprint getSprint() {
		return sprint;
	}

	public void setSprint(Sprint sprint) {
		this.sprint = sprint;
	}

	public boolean isConcluded() {
		return concluded;
	}

	public void setConcluded(boolean concluded) {
		this.concluded = concluded;
	}
}
