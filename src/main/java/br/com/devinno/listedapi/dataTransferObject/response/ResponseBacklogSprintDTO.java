package br.com.devinno.listedapi.dataTransferObject.response;

import java.time.LocalDate;

import br.com.devinno.listedapi.model.SprintBacklog;

public class ResponseBacklogSprintDTO {

	private Long id;
	
	private String name;
	
	private String description;
	
	private String metaSprint;
	
	private boolean concluded;
	
	private LocalDate dateStart;
	
	private LocalDate dateEnd;
	
	public ResponseBacklogSprintDTO() {
		
	}

	public ResponseBacklogSprintDTO(Long id, String name, String description, String metaSprint, boolean concluded,
			LocalDate dateStart, LocalDate dateEnd) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.metaSprint = metaSprint;
		this.concluded = concluded;
		this.dateStart = dateStart;
		this.dateEnd = dateEnd;
	}
	
	public ResponseBacklogSprintDTO(SprintBacklog sprint) {
		this.id = sprint.getSprint().getId();
		this.name = sprint.getSprint().getName();
		this.description = sprint.getSprint().getDescription();
		this.metaSprint = sprint.getSprint().getMetaSprint();
		this.concluded = sprint.isConcluded();
		this.dateStart = sprint.getSprint().getDateStart();
		this.dateEnd = sprint.getSprint().getDateEnd();
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
}
