package br.com.devinno.listedapi.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.devinno.listedapi.dataTransferObject.request.RequestInviteDTO;
import br.com.devinno.listedapi.service.InviteService;

@RestController
public class InviteController {

	@Autowired
	private InviteService service;
	
	@PostMapping("/projects/{id_project}/invite")
	public ResponseEntity<?> createInvite(HttpServletRequest request, @PathVariable(name = "id_project") Long idProject, @Valid @RequestBody RequestInviteDTO invite) {
		return this.service.createInvite(request, idProject, invite);
	}
	
	@GetMapping("/invites")
	public ResponseEntity<?> readInvites(HttpServletRequest request) {
		return this.service.getInvites(request);
	}
	
	@PutMapping("/invites/{invite_id}")
	public ResponseEntity<?> choiceInvite(HttpServletRequest request, @PathVariable(name = "invite_id") Long idInvite, @RequestParam("choice") String choice) {
		return this.service.updateStatusInvite(request, idInvite, choice);
	}
	
	@DeleteMapping("/invites/{invite_id}")
	public ResponseEntity<?> deleteInvite(HttpServletRequest request, @PathVariable("invite_id") Long idInvite){
		return this.service.deleteInvite(request, idInvite);
	}
}
