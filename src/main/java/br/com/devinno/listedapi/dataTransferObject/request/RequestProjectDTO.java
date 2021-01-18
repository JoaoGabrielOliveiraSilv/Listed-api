package br.com.devinno.listedapi.dataTransferObject.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class RequestProjectDTO {
	
	@Size(max = 100)
	@NotEmpty
	private String name;
	
	@Size(max = 255)
	private String description;
	
	@Min(1)
	@Max(4)
	private int weekSprint;
	
	public RequestProjectDTO() {
		
	}

	public RequestProjectDTO(String name, String description, int weekSprint) {
		this.name = name;
		this.description = description;
		this.weekSprint = weekSprint;
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

	public int getWeekSprint() {
		return weekSprint;
	}

	public void setWeekSprint(int weekSprint) {
		this.weekSprint = weekSprint;
	}
}
