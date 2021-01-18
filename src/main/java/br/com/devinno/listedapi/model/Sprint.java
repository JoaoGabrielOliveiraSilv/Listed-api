package br.com.devinno.listedapi.model;

import java.time.LocalDate;
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
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.com.devinno.listedapi.dataTransferObject.request.RequestSprintDTO;
import br.com.devinno.listedapi.dataTransferObject.request.RequestUpdateSprintDTO;

@Entity
@Table(name = "tb_sprint")
public class Sprint {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cd_sprint")
	private Long id;
	
	@Size(max = 100)
	@NotEmpty
	@Column(name = "nm_sprint")
	private String name;
	
	@Size(max = 255)
	@Column(name = "ds_sprint")
	private String description;
	
	@Size(max = 120)
	@NotEmpty
	@Column(name = "ds_meta_sprint")
	private String metaSprint;
	
	@NotNull
	@Column(name = "id_concluded")
	private boolean concluded;
	
	@NotNull
	@Column(name = "dt_start")
	private LocalDate dateStart;
	
	@NotNull
	@Column(name = "dt_end")
	private LocalDate dateEnd;
	
	@NotNull
	@Column(name = "qt_percentage")
	private int sprintPercentage;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "cd_project")
	private Project project;
	
	@OneToMany(mappedBy = "sprint")
	private List<SprintBacklog> sprintBacklogs;

	public Sprint() {
		
	}
	
	public Sprint(RequestSprintDTO requestSprint, Project project) {
		this.name = requestSprint.getName();
		this.description = requestSprint.getDescription();
		this.metaSprint = requestSprint.getMetaSprint();
		this.dateStart = LocalDate.now();
		this.dateEnd = this.dateStart.plusWeeks(project.getWeekSprint());
		this.concluded = false;
		this.project = project;
	}
	
	public Sprint(RequestSprintDTO requestSprint, Project project, List<SprintBacklog> sprintBacklogs) {
		this.name = requestSprint.getName();
		this.description = requestSprint.getDescription();
		this.metaSprint = requestSprint.getMetaSprint();
		this.dateStart = LocalDate.now();
		this.dateEnd = this.dateStart.plusWeeks(project.getWeekSprint());
		this.concluded = false;
		this.sprintBacklogs = sprintBacklogs;
		this.project = project;
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

	public String getMetaSprint() {
		return metaSprint;
	}

	public void setMetaSprint(String metaSprint) {
		this.metaSprint = metaSprint;
	}

	public boolean isConcluded() {
		return concluded;
	}

	public void setConcluded(boolean concluded) {
		this.concluded = concluded;
	}

	public LocalDate getDateStart() {
		return dateStart;
	}

	public void setDateStart(LocalDate dateStart) {
		this.dateStart = dateStart;
	}

	public LocalDate getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(LocalDate dateEnd) {
		this.dateEnd = dateEnd;
	}

	public int getSprintPercentage() {
		return sprintPercentage;
	}

	public void setSprintPercentage() {
		List<SprintBacklog> sprintBacklogs = this.getSprintBacklogs();
		
		float qtBacklogsConcludeds = 0;
		if(!sprintBacklogs.isEmpty()) {
			for(SprintBacklog item: sprintBacklogs) {
				if(item.isConcluded())
					qtBacklogsConcludeds++;
			}
			
			int percentage = (int) ((qtBacklogsConcludeds / (float) sprintBacklogs.size()) * 100);
			this.sprintPercentage = percentage;
		}else {
			this.sprintPercentage = 0;
		}
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
	
	public void populate(RequestUpdateSprintDTO sprint) {
		if(sprint.getName() != null && !sprint.getName().equals(" "))
			this.name = sprint.getName();
		if(sprint.getDescription() != null)
			this.description = sprint.getDescription();
		if(sprint.getMetaSprint() != null && !sprint.getMetaSprint().equals(" "))
			this.metaSprint = sprint.getMetaSprint();
	}
}
