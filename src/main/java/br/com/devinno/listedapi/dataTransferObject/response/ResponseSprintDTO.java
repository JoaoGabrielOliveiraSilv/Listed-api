package br.com.devinno.listedapi.dataTransferObject.response;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import br.com.devinno.listedapi.model.Sprint;
import br.com.devinno.listedapi.model.SprintBacklog;

public class ResponseSprintDTO {

	private Long id;
	
	private String name;
	
	private String description;
	
	private String metaSprint;
	
	private boolean concluded;
	
	private LocalDate dateStart;
	
	private LocalDate dateEnd;
	
	private int sprintPercentage;
	
	private List<ResponseSprintBacklogDTO> sprintBacklogs = new ArrayList<ResponseSprintBacklogDTO>();
	
	public ResponseSprintDTO() {
		
	}

	public ResponseSprintDTO(Long id, String name, String description, String metaSprint, boolean concluded,
			LocalDate dateStart, LocalDate dateEnd, int sprintPercentage, List<SprintBacklog> sprintBacklogs) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.metaSprint = metaSprint;
		this.concluded = concluded;
		this.dateStart = dateStart;
		this.dateEnd = dateEnd;
		this.sprintPercentage = sprintPercentage;
		
		for(SprintBacklog item: sprintBacklogs)
			this.sprintBacklogs.add(new ResponseSprintBacklogDTO(item));
	}
	
	public ResponseSprintDTO(Sprint sprint) {
		this.id = sprint.getId();
		this.name = sprint.getName();
		this.description = sprint.getDescription();
		this.metaSprint = sprint.getMetaSprint();
		this.concluded = sprint.isConcluded();
		this.dateStart = sprint.getDateStart();
		this.dateEnd = sprint.getDateEnd();
		this.sprintPercentage = sprint.getSprintPercentage();
		
		if(!sprint.getSprintBacklogs().isEmpty()) {
			for(SprintBacklog item: sprint.getSprintBacklogs()) {
				ResponseSprintBacklogDTO response = new ResponseSprintBacklogDTO(item);
				this.sprintBacklogs.add(response);
			}
		}
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

	public List<ResponseSprintBacklogDTO> getSprintBacklogs() {
		return sprintBacklogs;
	}

	public void setSprintBacklogs(List<SprintBacklog> sprintBacklogs) {
		for(SprintBacklog item: sprintBacklogs) {
			ResponseSprintBacklogDTO response = new ResponseSprintBacklogDTO(item);
			this.sprintBacklogs.add(response);
		}
	}

	public int getSprintPercentage() {
		return sprintPercentage;
	}

	public void setSprintPercentage(int sprintPercentage) {
		this.sprintPercentage = sprintPercentage;
	}
}
