package br.com.devinno.listedapi.dataTransferObject.request;

import javax.validation.constraints.NotNull;

public class RequestSprintBacklogDTO {

	@NotNull
	private Long[] backlogs;

	public RequestSprintBacklogDTO() {
	}
	
	public RequestSprintBacklogDTO(Long[] backlogs) {
		this.backlogs = backlogs;
	}

	public Long[] getBacklogs() {
		return backlogs;
	}

	public void setBacklogs(Long[] backlogs) {
		this.backlogs = backlogs;
	}
}
