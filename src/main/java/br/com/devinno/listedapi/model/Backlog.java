package br.com.devinno.listedapi.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.com.devinno.listedapi.dataTransferObject.request.RequestBacklogDTO;
import br.com.devinno.listedapi.dataTransferObject.request.RequestUpdateBacklogDTO;

@Entity
@Table(name = "tb_backlog")
public class Backlog {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cd_backlog")
	private Long id;
	
	@Size(max = 100)
	@NotEmpty
	@Column(name = "nm_backlog")
	private String name;
	
	@Size(max = 255)
	@Column(name = "ds_backlog")
	private String description;
	
	@Min(1)
	@NotNull
	@Column(name = "qt_priority")
	private int priority;
	
	@Min(1)
	@Max(5)
	@NotNull
	@Column(name = "qt_difficulty")
	private int difficulty;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "cd_project")
	private Project project;
	
	@OneToMany(mappedBy = "backlog")
	private List<SprintBacklog> sprintBacklogs;
	
	public Backlog() {
		
	}
		
	public Backlog(String name, String description, int priority, boolean concluded, int difficulty,
			Project project, List<SprintBacklog> sprintBacklogs) {
		this.name = name;
		this.description = description;
		this.priority = priority;
		this.difficulty = difficulty;
		this.project = project;
		this.sprintBacklogs = sprintBacklogs;
	}

	public Backlog(RequestBacklogDTO Backlog) {
		this.name = Backlog.getName();
		this.description = Backlog.getDescription();
		this.priority = Backlog.getPriority();
		this.difficulty = Backlog.getDifficulty();	
	}
	
	public Backlog(RequestBacklogDTO Backlog, Project project) {
		this.name = Backlog.getName();
		this.description = Backlog.getDescription();
		this.priority = Backlog.getPriority();
		this.difficulty = Backlog.getDifficulty();
		this.project = project;
	}
	
	public Backlog(RequestBacklogDTO Backlog, Project project, List<SprintBacklog> sprints) {
		this.name = Backlog.getName();
		this.description = Backlog.getDescription();
		this.priority = Backlog.getPriority();
		this.difficulty = Backlog.getDifficulty();
		this.project = project;
		this.sprintBacklogs = sprints;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public int getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public List<SprintBacklog> getSprintBacklogs() {
		return sprintBacklogs;
	}

	public void setSprintBacklogs(List<SprintBacklog> sprintBacklogs) {
		this.sprintBacklogs = sprintBacklogs;
	}
	
	public void populate(RequestUpdateBacklogDTO backlog) {
		if(backlog.getName() != null && !backlog.getName().equals(" "))
			this.name = backlog.getName();
		if(backlog.getDescription() != null)
			this.description = backlog.getDescription();
		if(backlog.getPriority() != 0)	
			this.priority = backlog.getPriority();
		if(backlog.getDifficulty() != 0)
		this.difficulty = backlog.getDifficulty();
	}
}
