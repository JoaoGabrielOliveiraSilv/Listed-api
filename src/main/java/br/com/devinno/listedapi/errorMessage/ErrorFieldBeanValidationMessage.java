package br.com.devinno.listedapi.errorMessage;

public class ErrorFieldBeanValidationMessage {

	private String field;
	
	private String message;
	
	public ErrorFieldBeanValidationMessage(String field, String message) {
		this.field = field;
		this.message = message;
	}
	
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
