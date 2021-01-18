package br.com.devinno.listedapi.dataTransferObject.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


public class RequestBacklogDTO {
	
	@Size(max = 100)
	@NotEmpty
	private String name;
	
	@Size(max = 255)
	private String description;
	
	@Min(1)
	@NotNull
	private int priority;
	
	@Min(1)
	@Max(5)
	@NotNull
	private int difficulty;
	
	public RequestBacklogDTO() {
		
	}
	
	public RequestBacklogDTO(String name, String description, int priority, int difficulty) {
		this.name = name;
		this.description = description;
		this.priority = priority;
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

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public int getDifficulty() {
		return difficulty;
	}

	public void setDurationEstimated(int difficulty) {
		this.difficulty = difficulty;
	}
}
