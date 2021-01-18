package br.com.devinno.listedapi.dataTransferObject.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class RequestSprintDTO {	
	
	@Size(max = 100)
	@NotEmpty
	private String name;
	
	@Size(max = 255)
	private String description;
	
	@Size(max = 120)
	@NotEmpty
	private String metaSprint;
	
	private Long[] backlogs;  

	public RequestSprintDTO() {
		
	}
	
	public RequestSprintDTO(String name, String description, String metaSprint, Long[] backlogs) {
		this.name = name;
		this.description = description;
		this.metaSprint = metaSprint;
		this.backlogs = backlogs;
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

	public Long[] getBacklogs() {
		return backlogs;
	}

	public void setBacklogs(Long[] backlogs) {
		this.backlogs = backlogs;
	}
}
