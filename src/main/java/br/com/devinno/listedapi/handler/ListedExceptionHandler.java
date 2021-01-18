package br.com.devinno.listedapi.handler;

import java.util.ArrayList;
import java.util.List;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;

import com.auth0.jwt.exceptions.JWTVerificationException;

import br.com.devinno.listedapi.errorMessage.ErrorFieldBeanValidationMessage;
import br.com.devinno.listedapi.errorMessage.ErrorMessageBeanValidation;
import br.com.devinno.listedapi.errorMessage.ErrorResponse;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ListedExceptionHandler {
	
	@ExceptionHandler(JWTVerificationException.class)
	public ResponseEntity<?> handleSignatureVerificationException(
			JWTVerificationException ex) {
		
		ErrorResponse error = new ErrorResponse(HttpStatus.UNAUTHORIZED, null);
		
		return ResponseHandler.toResponseEntity(error, HttpStatus.UNAUTHORIZED);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> handleMethodArgumentNotValid(
			MethodArgumentNotValidException ex) {

		
		ErrorResponse error = new ErrorResponse("Preenchimento de campos inválidos",
				"Algum campo não está devidamente preenchido", 
				HttpStatus.BAD_REQUEST);


		ErrorMessageBeanValidation errorBeanValidation = new ErrorMessageBeanValidation(error, this.getFieldsError(ex));
		
		return ResponseHandler.toResponseEntity(errorBeanValidation, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(MultipartException.class)
	public ResponseEntity<?> handleMultipartException(MultipartException ex) {

		ErrorResponse error = new ErrorResponse("Multipart Obrigatório",
				"Esta requisição neste endpoint pede o uso do multipart. Consulte nossa documentação para masi informaçoes.",
				HttpStatus.BAD_REQUEST);
		
		return ResponseHandler.toResponseEntity(error, HttpStatus.BAD_REQUEST);
	}
	
	private List<ErrorFieldBeanValidationMessage> getFieldsError(MethodArgumentNotValidException ex) {

		List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
		List<ErrorFieldBeanValidationMessage> errorFieldResponse = new ArrayList<ErrorFieldBeanValidationMessage>();
		
		fieldErrors.forEach(item -> errorFieldResponse.add(new ErrorFieldBeanValidationMessage(item.getField(), item.getDefaultMessage())));
		
		return errorFieldResponse;
	}
}
