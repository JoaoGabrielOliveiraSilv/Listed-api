package br.com.devinno.listedapi.model;

import java.time.LocalDate;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import br.com.devinno.listedapi.dataTransferObject.request.RequestProjectDTO;
import br.com.devinno.listedapi.dataTransferObject.request.RequestUpdateProjectDTO;

@Entity
@Table(name = "tb_project")
public class Project {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cd_project")
	private Long id;
	
	@Size(max = 100)
	@NotEmpty
	@Column(name = "nm_project")
	private String name;
	
	@Size(max = 255)
	@Column(name = "ds_project")
	private String description;
	
	@Min(1)
	@Max(4)
	@Column(name = "qt_week_sprint")
	private int weekSprint;
	
	@Column(name = "id_concluded")
	private boolean concluded;
	
	@Column(name = "dt_start")
	private LocalDate dateStart;
	
	@Column(name = "dt_end")
	private LocalDate dateEnd;
	
	@OneToMany(mappedBy = "project")
	private List<Access> access;
	
	public Project() {
		
	}
	
	public Project(RequestProjectDTO project) {
		this.name = project.getName();
		this.description = project.getDescription();
		this.weekSprint = project.getWeekSprint();
		this.dateStart = LocalDate.now();
		this.concluded = false;
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

	public int getWeekSprint() {
		return weekSprint;
	}

	public void setWeekSprint(int weekSprint) {
		this.weekSprint = weekSprint;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public List<Access> getAccess() {
		return access;
	}

	public void setAccess(List<Access> access) {
		this.access = access;
	}

	public void populate(RequestUpdateProjectDTO project) {
		if(project.getName() != null && !project.getName().equals(" "))
			this.name = project.getName();
		if(project.getDescription() != null)
			this.description = project.getDescription();
		if(project.getWeekSprint() > 0 && this.getWeekSprint() < 4)
			this.weekSprint = project.getWeekSprint();
	}
}
