package br.com.devinno.listedapi.model;

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

import br.com.devinno.listedapi.dataTransferObject.request.RequestTaskDTO;
import br.com.devinno.listedapi.dataTransferObject.request.RequestUpdateTaskDTO;

import javax.validation.constraints.NotNull;


@Entity
@Table(name = "tb_task")
public class Task {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cd_task")
	private Long id;
	
	@Size(max = 100)
	@NotEmpty
	@Column(name = "nm_task")
	private String name;
	
	@NotNull
	@Column(name = "id_concluded")
	private boolean concluded;

	@ManyToOne
	@JoinColumn(name = "cd_backlog")
	private Backlog backlog;
	
	@ManyToOne
	@JoinColumn(name = "cd_access")
	private Access assigned;

	public Task() {
		
	}
	
	public Task(RequestTaskDTO Task, Backlog backlog, Access assigned) {
		this.name = Task.getName();
		this.concluded = false;
		this.backlog = backlog;
		this.assigned = assigned;
	}
	
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

	public boolean isConcluded() {
		return concluded;
	}

	public void setConcluded(boolean concluded) {
		this.concluded = concluded;
	}

	public Backlog getBacklog() {
		return backlog;
	}

	public void setBacklog(Backlog backlog) {
		this.backlog = backlog;
	}
	
	public Access getAssigned() {
		return assigned;
	}

	public void setAssigned(Access assigned) {
		this.assigned = assigned;
	}

	public void populate(RequestUpdateTaskDTO task) {
		if(task.getName() != null && !task.getName().equals(" "))
			this.name = task.getName();
	}
}
