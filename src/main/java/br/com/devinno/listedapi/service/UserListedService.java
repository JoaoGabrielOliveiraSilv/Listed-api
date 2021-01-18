package br.com.devinno.listedapi.service;

import static br.com.devinno.listedapi.JWT.SecurityConstants.EXPIRATION_TIME;
import static br.com.devinno.listedapi.JWT.SecurityConstants.SECRET_REGISTER;
import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;

import br.com.devinno.listedapi.dataTransferObject.request.RequestUserListedDTO;
import br.com.devinno.listedapi.dataTransferObject.response.ResponseTokenDTO;
import br.com.devinno.listedapi.dataTransferObject.response.ResponseUserListedDTO;
import br.com.devinno.listedapi.errorMessage.ErrorResponse;
import br.com.devinno.listedapi.handler.ResponseHandler;
import br.com.devinno.listedapi.model.UserListed;
import br.com.devinno.listedapi.repository.UserListedRepository;

@Service
public class UserListedService {

	@Autowired
	private UserListedRepository repository;

	public ResponseEntity<?> save(RequestUserListedDTO newUser) {
		// Assigns the user the encrypted password
		newUser.setPassword(new BCryptPasswordEncoder().encode(newUser.getPassword()));

		// Checks if the username already exists
		if(!newUser.getUsername().startsWith("@")) 
			newUser.setUsername("@".concat(newUser.getUsername().toLowerCase()));

		// Search for a user with a username or email sent
		Optional<UserListed> found = repository.findByUsernameOrEmail(newUser.getUsername(), newUser.getEmail());

		// Checks existing email/username
		if(!found.isPresent()) {
			UserListed user = new UserListed(newUser);
			user.setUsername(user.getUsername().toLowerCase());
			user.setEmail(user.getEmail().toLowerCase());
			repository.save(user);

			// Generate a token
			ResponseTokenDTO token = new ResponseTokenDTO(
					JWT.create()
					.withSubject(user.getEmail())
					.withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
					.sign(HMAC512(SECRET_REGISTER.getBytes()))
					);

			return ResponseHandler.toResponseEntity(token, HttpStatus.CREATED);
		} else {
			String fieldInvalid;

			// Checks which field is invalid
			if(newUser.getEmail().equalsIgnoreCase(found.get().getEmail()) && newUser.getUsername().equalsIgnoreCase(found.get().getUsername())) {
				fieldInvalid =  "Este username e email já estão sendo utilizados";
			} else if(newUser.getUsername().equalsIgnoreCase(found.get().getUsername())) {
				fieldInvalid = "Este username já está sendo utilizados";
			} else {
				fieldInvalid = "Este email já está sendo utilizados";
			}

			// Sets up error message
			ErrorResponse error = new ErrorResponse();

			error.setTitle("Este " + fieldInvalid + " já está em uso");
			error.setMessage(fieldInvalid);
			error.setStatus(HttpStatus.CONFLICT);

			return ResponseHandler.toResponseEntity(error, HttpStatus.CONFLICT);
		}
	}

	public ResponseEntity<?> findByUsernameOrEmail(String username) {
		if(!username.contains("@"))
			username = "@" + username;
		
		Optional<UserListed> user = repository.findByUsernameOrEmail(username.toLowerCase(), username.toLowerCase());

		if(!user.isPresent()) {
			// Sets up error message
			ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND, "user");
			return ResponseHandler.toResponseEntity(error, HttpStatus.NOT_FOUND);
		}

		// Convert to response object
		ResponseUserListedDTO userResponse = new ResponseUserListedDTO(user.get());

		return ResponseHandler.toResponseEntity(userResponse, HttpStatus.OK);
	}
}
