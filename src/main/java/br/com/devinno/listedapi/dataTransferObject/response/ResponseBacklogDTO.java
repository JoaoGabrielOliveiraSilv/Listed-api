package br.com.devinno.listedapi.dataTransferObject.response;

import java.util.List;

import br.com.devinno.listedapi.model.Backlog;
import br.com.devinno.listedapi.model.SprintBacklog;

public class ResponseBacklogDTO {

	private Long id;
	
	private String name;
	
	private String description;
	
	private int priority;
	
	private boolean concluded;
	
	private int difficulty;
	
	public ResponseBacklogDTO() {
		
	}

	public ResponseBacklogDTO(Long id, String name, String description, int priority, boolean concluded,
			int difficulty, List<ResponseBacklogSprintDTO> sprints) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.priority = priority;
		this.concluded = concluded;
		this.difficulty = difficulty;
	}
	
	public ResponseBacklogDTO(Backlog backlog) {
		this.id = backlog.getId();
		this.name = backlog.getName();
		this.description = backlog.getDescription();
		this.priority = backlog.getPriority();
		this.difficulty = backlog.getDifficulty();
		
		if(backlog.getSprintBacklogs() != null) {
			for(SprintBacklog item: backlog.getSprintBacklogs()) {
				if(item.isConcluded()) 
					this.concluded = true;
			}
		}
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
