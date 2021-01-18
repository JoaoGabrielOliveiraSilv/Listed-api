package br.com.devinno.listedapi.dataTransferObject.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class RequestTaskDTO {

	@Size(max = 100)
	@NotEmpty
	private String name;

	public RequestTaskDTO() {
		
	}

	public RequestTaskDTO(String name, String description, int difficulty) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
