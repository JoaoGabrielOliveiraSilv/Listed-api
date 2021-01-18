package br.com.devinno.listedapi.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseHandler {
	
	public static ResponseEntity<?> toResponseEntity(Object body, HttpStatus status) {
		String content;
		
		if(status.value() >= 400)
			content = "application/problem+json";
		else
			content = "application/json";
		
		return ResponseEntity
				.status(status.value())
				.header("Content-Type", content)
				.body(body);
	}
}
