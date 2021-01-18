package br.com.devinno.listedapi.dataTransferObject.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

public class RequestUpdateProjectDTO {

	@Size(max = 100)
	private String name;
	
	@Size(max = 255)
	private String description;
	
	@Min(0)
	@Max(4)
	private int weekSprint;

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
