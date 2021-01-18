package br.com.devinno.listedapi.dataTransferObject.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

public class RequestUpdateBacklogDTO {

	@Size(max = 100)
	private String name;
	
	@Size(max = 255)
	private String description;
	
	@Min(0)
	@Max(5)
	private int difficulty;
	
	@Min(0)
	private int priority;
	
	public RequestUpdateBacklogDTO() {
		
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

	public int getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}
}
