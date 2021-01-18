package br.com.devinno.listedapi.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.devinno.listedapi.dataTransferObject.request.RequestUpdatePasswordDTO;
import br.com.devinno.listedapi.dataTransferObject.request.RequestUpdateUserListedDTO;
import br.com.devinno.listedapi.service.ProfileService;

@RestController
@RequestMapping("/profile")
public class ProfileController {

	@Autowired
	private ProfileService service;

	@PostMapping
	public ResponseEntity<?> uploadProfilePicture(HttpServletRequest request, @RequestParam("image") MultipartFile multipartFile) throws IOException {
		return this.service.uploadProfileImage(request, multipartFile);
	}

	@GetMapping
	public ResponseEntity<?> readProfile(HttpServletRequest request) throws IOException {
		return this.service.findProfile(request);
	}

	@PutMapping
	public ResponseEntity<?> updateProfile(HttpServletRequest request, @RequestBody RequestUpdateUserListedDTO newUser) throws IOException {
		return this.service.updateProfile(request, newUser);
	}

	@PatchMapping
	public ResponseEntity<?> updatePassword(HttpServletRequest request, @Valid @RequestBody RequestUpdatePasswordDTO passwordObject) throws IOException {
		return this.service.updatePassword(request, passwordObject);
	}

	@DeleteMapping
	public ResponseEntity<?> deleteProfile(HttpServletRequest request) throws IOException {
		return this.service.deleteProfile(request);
	}
}