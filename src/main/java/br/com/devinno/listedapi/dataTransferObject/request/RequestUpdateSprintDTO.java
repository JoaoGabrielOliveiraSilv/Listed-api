package br.com.devinno.listedapi.dataTransferObject.request;

import javax.validation.constraints.Size;


public class RequestUpdateSprintDTO {
	
	@Size(max = 100)
	private String name;
	
	@Size(max = 255)
	private String description;
	
	@Size(max = 120)
	private String metaSprint;
	
	private Long[] backlogs;
	
	public RequestUpdateSprintDTO() {
		
	}

	public RequestUpdateSprintDTO(String name, String description, String metaSprint, Long[] backlogs) {
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
