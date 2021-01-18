package br.com.devinno.listedapi.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.devinno.listedapi.dataTransferObject.request.RequestUserListedDTO;
import br.com.devinno.listedapi.service.UserListedService;

@RestController
@RequestMapping("/users")
public class UserListedController {

	@Autowired
	private UserListedService service;

	@PostMapping
	public ResponseEntity<?> createUser(HttpServletRequest request, HttpServletResponse response, @Valid @RequestBody RequestUserListedDTO user) throws Exception {
		return this.service.save(user);
	}

	@GetMapping("/{username}")
	public ResponseEntity<?> readOneUser(@PathVariable("username") String username) {
		return this.service.findByUsernameOrEmail(username);
	}
}
