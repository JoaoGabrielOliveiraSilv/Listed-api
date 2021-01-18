package br.com.devinno.listedapi.dataTransferObject.response;

import br.com.devinno.listedapi.model.Backlog;
import br.com.devinno.listedapi.model.Task;

public class ResponseTaskDTO {

	private Long id;
	
	private String name;
	
	private boolean concluded;
	
	private ResponseAccessDTO assigned;
	
	public ResponseTaskDTO() {
		
	}

	public ResponseTaskDTO(Task task) {
		this.id = task.getId();
		this.name = task.getName();
		this.concluded = task.isConcluded();
		if(task.getAssigned() != null)
			this.assigned = new ResponseAccessDTO(task.getAssigned());
	}
	
	public ResponseTaskDTO(Long id, String name, String description, boolean concluded, int difficulty, Backlog backlog) {
		this.id = id;
		this.name = name;
		this.concluded = concluded;
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
	
	public boolean isConcluded() {
		return concluded;
	}

	public void setConcluded(boolean concluded) {
		this.concluded = concluded;
	}

	public ResponseAccessDTO getAssigned() {
		return assigned;
	}

	public void setAssigned(ResponseAccessDTO assigned) {
		this.assigned = assigned;
	}
}
