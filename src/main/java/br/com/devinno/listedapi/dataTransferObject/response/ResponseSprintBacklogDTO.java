package br.com.devinno.listedapi.dataTransferObject.response;

import br.com.devinno.listedapi.model.SprintBacklog;

public class ResponseSprintBacklogDTO {

	private Long id;
	
	private String name;
	
	private String description;
	
	private int priority;
	
	private int difficulty;
	
	private boolean concluded;
	
	public ResponseSprintBacklogDTO() {
		
	}

	public ResponseSprintBacklogDTO(Long id, String name, String description, int priority, boolean concluded,
			int difficulty) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.priority = priority;
		this.concluded = concluded;
		this.difficulty = difficulty;
	}
	
	public ResponseSprintBacklogDTO(SprintBacklog sprintBacklog) {
		this.id = sprintBacklog.getBacklog().getId();
		this.name = sprintBacklog.getBacklog().getName();
		this.description = sprintBacklog.getBacklog().getDescription();
		this.priority = sprintBacklog.getBacklog().getPriority();
		this.difficulty = sprintBacklog.getBacklog().getDifficulty();
		this.concluded = sprintBacklog.isConcluded();
	}
	
	public Long getId() {
		return this.id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public boolean isConcluded() {
		return concluded;
	}

	public void setConcluded(boolean concluded) {
		this.concluded = concluded;
	}

	public int getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
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
}
