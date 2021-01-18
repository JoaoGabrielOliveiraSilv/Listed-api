package br.com.devinno.listedapi.service;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.devinno.listedapi.JWT.JWTTools;
import br.com.devinno.listedapi.dataTransferObject.request.RequestUpdatePasswordDTO;
import br.com.devinno.listedapi.dataTransferObject.request.RequestUpdateUserListedDTO;
import br.com.devinno.listedapi.dataTransferObject.response.ResponseProfileDTO;
import br.com.devinno.listedapi.errorMessage.ErrorResponse;
import br.com.devinno.listedapi.handler.ResponseHandler;
import br.com.devinno.listedapi.model.UserListed;
import br.com.devinno.listedapi.repository.AccessRepository;
import br.com.devinno.listedapi.repository.InviteRepository;
import br.com.devinno.listedapi.repository.NotificationRepository;
import br.com.devinno.listedapi.repository.UserListedRepository;

@Service
public class ProfileService {

	@Autowired
	private UserListedRepository repository;

	@Autowired
	private NotificationRepository notificationRepository;

	@Autowired
	private AccessRepository accessRepository;
	
	@Autowired
	private InviteRepository inviteRepository;
	
	@Autowired
	private AmazonService amazonService;

	@Autowired
	private JWTTools tokenTools;

	public ResponseEntity<?> uploadProfileImage(HttpServletRequest request, MultipartFile file) {
		Optional<UserListed> user = tokenTools.findUserByToken(request);

		if(!user.isPresent()) {
			ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND, "user");

			return ResponseHandler.toResponseEntity(error, HttpStatus.NOT_FOUND);
		} else {
			Object response = this.amazonService.uploadImageProfile(user, file);

			if(response.getClass().getName().toLowerCase().contains("userlisted")) {
				UserListed userL = (UserListed) response;
				this.repository.save(userL);

				ResponseProfileDTO profileResponse = new ResponseProfileDTO(userL);

				return ResponseHandler.toResponseEntity(profileResponse, HttpStatus.OK);
			}

			ErrorResponse error = (ErrorResponse) response;
			return ResponseHandler.toResponseEntity(error, error.getStatus());
		}
	}

	public ResponseEntity<?> findProfile(HttpServletRequest request) {
		Optional<UserListed> user = tokenTools.findUserByToken(request);

		if(!user.isPresent()) {
			ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND, "user");

			return ResponseHandler.toResponseEntity(error, HttpStatus.NOT_FOUND);
		}

		ResponseProfileDTO profileResponse = new ResponseProfileDTO(user.get());

		return ResponseHandler.toResponseEntity(profileResponse, HttpStatus.OK);
	}

	public ResponseEntity<?> updateProfile(HttpServletRequest request, RequestUpdateUserListedDTO newUser) throws IOException{
		Optional<UserListed> user = tokenTools.findUserByToken(request);

		Optional<UserListed> searchUser;

		// Check if username and email are available
		if(newUser.getUsername() != null || newUser.getEmail() != null) {
			searchUser = repository.findByUsernameOrEmail(newUser.getUsername(), newUser.getEmail());
		} else {
			searchUser = repository.findById(user.get().getId());
		}

		if(searchUser.isPresent()) {
			if(searchUser.get().getId() != user.get().getId()) {
				String fieldInvalid;

				//Check which field is invalid
				if(user.get().getEmail().equalsIgnoreCase(searchUser.get().getEmail()) && user.get().getUsername().equalsIgnoreCase(searchUser.get().getUsername())) {
					fieldInvalid =  "username e email";
				} else if(searchUser.get().getUsername().equalsIgnoreCase(searchUser.get().getUsername())) {
					fieldInvalid = "username";
				} else {
					fieldInvalid = "email";
				}

				ErrorResponse error = new ErrorResponse();

				error.setTitle("Este(s) " + fieldInvalid + " já está em uso");
				error.setMessage("O(s) campo(s) " + fieldInvalid + " já se encontram cadastrados em nosso banco.");
				error.setStatus(HttpStatus.CONFLICT);

				return ResponseHandler.toResponseEntity(error, HttpStatus.CONFLICT);
			}
		} 
		
		user.get().populate(newUser);
		repository.save(user.get());

		ResponseProfileDTO profileResponse = new ResponseProfileDTO(user.get());

		return ResponseHandler.toResponseEntity(profileResponse, HttpStatus.OK);
	}

	public ResponseEntity<?> updatePassword(HttpServletRequest request, RequestUpdatePasswordDTO passwordObject) throws IOException {
		Optional<UserListed> user = tokenTools.findUserByToken(request);

		BCryptPasswordEncoder crypt = new BCryptPasswordEncoder();

		// Check if password it's correct
		if(!crypt.matches(passwordObject.getPassword(), user.get().getPassword())) {

			ErrorResponse error = new ErrorResponse();
			
			error.setTitle("Senha incorreta");
			error.setMessage("A senha não é a mesma que a cadastrada");
			error.setStatus(HttpStatus.BAD_REQUEST);
			
			return ResponseHandler.toResponseEntity(error, HttpStatus.BAD_REQUEST);
		}

		// Encrypt and save the password in the bank
		user.get().setPassword(crypt.encode(passwordObject.getNewPassword()));
		this.repository.save(user.get());
		
		return ResponseHandler.toResponseEntity(null, HttpStatus.OK);
	}

	@Transactional
	public ResponseEntity<?> deleteProfile(HttpServletRequest request) throws IOException {
		Optional<UserListed> user = tokenTools.findUserByToken(request);

		this.notificationRepository.deleteAllByUser(user.get());
		this.inviteRepository.deleteAllByUser(user.get());
		this.accessRepository.deleteAllByUser(user.get());
		this.repository.deleteById(user.get().getId());

		JSONObject message = new JSONObject();
		message.put("message", "Conta deletada com sucesso!");

		return ResponseHandler.toResponseEntity(message.toMap(), HttpStatus.OK);
	}
}
