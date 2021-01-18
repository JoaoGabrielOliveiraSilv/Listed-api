package br.com.devinno.listedapi.errorMessage;

import java.util.List;

public class ErrorMessageBeanValidation {

	private ErrorResponse error;
	
	private List<ErrorFieldBeanValidationMessage> errorFields;

	public ErrorMessageBeanValidation(ErrorResponse error, List<ErrorFieldBeanValidationMessage> errorFields) {
		this.error = error;
		this.errorFields = errorFields;
	}

	public ErrorResponse getError() {
		return error;
	}

	public void setError(ErrorResponse error) {
		this.error = error;
	}

	public List<ErrorFieldBeanValidationMessage> getErrorFields() {
		return errorFields;
	}

	public void setErrorFields(List<ErrorFieldBeanValidationMessage> errorFields) {
		this.errorFields = errorFields;
	}
}
