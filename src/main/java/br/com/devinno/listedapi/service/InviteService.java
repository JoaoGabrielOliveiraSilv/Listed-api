package br.com.devinno.listedapi.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.devinno.listedapi.JWT.JWTTools;
import br.com.devinno.listedapi.dataTransferObject.request.RequestInviteDTO;
import br.com.devinno.listedapi.dataTransferObject.response.ResponseInviteDTO;
import br.com.devinno.listedapi.errorMessage.ErrorResponse;
import br.com.devinno.listedapi.handler.ResponseHandler;
import br.com.devinno.listedapi.model.Access;
import br.com.devinno.listedapi.model.Invite;
import br.com.devinno.listedapi.model.Project;
import br.com.devinno.listedapi.model.UserListed;
import br.com.devinno.listedapi.repository.AccessRepository;
import br.com.devinno.listedapi.repository.InviteRepository;
import br.com.devinno.listedapi.repository.ProjectRepository;
import br.com.devinno.listedapi.repository.UserListedRepository;

@Service
public class InviteService {

	@Autowired
	private JWTTools tokenTools;
	
	@Autowired
	private InviteRepository repository;
	
	@Autowired
	private UserListedRepository userRepository;
	
	@Autowired
	private AccessRepository accessRepository;
	
	@Autowired
	private ProjectRepository projectRepository;
	
	private JSONObject message = new JSONObject();
	
	public ResponseEntity<?> createInvite(HttpServletRequest request, Long idProject, RequestInviteDTO invite) {
		Optional<Project> project = this.projectRepository.findById(idProject);
		
		if(!project.isPresent()) {
			ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND, "project");
			return ResponseHandler.toResponseEntity(error, HttpStatus.NOT_FOUND);
		}
		
		ResponseEntity<?> projectConcluded = this.projectConcluded(idProject);
		if(projectConcluded != null)
			return projectConcluded;
		
		// Check if user allowed
		if(!this.tokenTools.permissionAction(request, idProject, "PO")) {
			ErrorResponse error = new ErrorResponse(HttpStatus.FORBIDDEN, null);
			return ResponseHandler.toResponseEntity(error, HttpStatus.FORBIDDEN);
		}

		if(this.whatIsRole(invite.getRole()).equalsIgnoreCase("invalid")) {
			ErrorResponse error = new ErrorResponse();
			error.setTitle("Sigla de função inválida");
			error.setMessage("Está sigla de função não é válida! Consulte nossa documentação.");
			error.setStatus(HttpStatus.BAD_REQUEST);
			
			return ResponseHandler.toResponseEntity(error, HttpStatus.BAD_REQUEST);
		}

		String verification = this.validationUser(invite.getUsername(), idProject);

		// Filter possible errors
		switch(verification) {
			case "not-found":
				ErrorResponse errorNF = new ErrorResponse(HttpStatus.NOT_FOUND, "user");
				return ResponseHandler.toResponseEntity(errorNF, HttpStatus.NOT_FOUND);

			case "conflict":
				ErrorResponse errorC = new ErrorResponse();
				
				errorC.setTitle("Usuário inválido para convite");
				errorC.setMessage("Este usuário já está inserido neste projeto");
				errorC.setStatus(HttpStatus.CONFLICT);
				
				return ResponseHandler.toResponseEntity(errorC, HttpStatus.CONFLICT);
		}
				
		Optional<UserListed> sender = this.tokenTools.findUserByToken(request);
		Optional<UserListed> guest = this.userRepository.findByUsernameOrEmail(invite.getUsername(), invite.getUsername());
		Optional<Access> accessInvite = this.accessRepository.findByUserAndProject(sender.get(), project.get());
		
		List<Invite> invitesGuest = this.repository.findByUser(guest.get());

		//Checks if the user no longer has a pending invite for that project
		for(Invite item: invitesGuest) {
			if(item.getAccess().getProject() == project.get() 
					&& item.getUser() == guest.get() 
					&& item.getStatus().equalsIgnoreCase("pendente")) {

				ErrorResponse error = new ErrorResponse("Usuário indisponível para convite", 
						"Este usuário já possui um convite pendente deste projeto", HttpStatus.CONFLICT);
				return ResponseHandler.toResponseEntity(error, HttpStatus.CONFLICT);
			}
		}

		Invite inviteSave = new Invite();
		inviteSave.setName("Você recebeu um convite!");
		inviteSave.setDescription(sender.get().getName() + " está te convidando para ser " 
				+ this.whatIsRole(invite.getRole()) + " no projeto '" + project.get().getName() + "'.");
		inviteSave.setRole(this.whatIsRole(invite.getRole()));
		inviteSave.setDate(LocalDate.now());
		inviteSave.setStatus("pendente");
		inviteSave.setAccess(accessInvite.get());
		inviteSave.setUser(guest.get());
		
		this.repository.save(inviteSave);
		
		this.message.put("message", "Usuário convidade com sucesso");
		
		return ResponseHandler.toResponseEntity(this.message.toMap(), HttpStatus.OK);
	}
	
	public ResponseEntity<?> getInvites(HttpServletRequest request) {
		Optional<UserListed> user = tokenTools.findUserByToken(request);
		List<Invite> invites = this.repository.findByUser(user.get());
		
		if(invites.isEmpty()) {
			return ResponseHandler.toResponseEntity(invites, HttpStatus.OK);
		}
		
		List<ResponseInviteDTO> responseInvites = new ArrayList<ResponseInviteDTO>();
		
		for(Invite item: invites) {
			responseInvites.add(new ResponseInviteDTO(item));
		}
		return ResponseHandler.toResponseEntity(responseInvites, HttpStatus.OK);
	}
	
	public ResponseEntity<?> updateStatusInvite(HttpServletRequest request, Long idInvite, String choice) {
		choice = choice.toLowerCase();
		Optional<Invite> invite = this.repository.findById(idInvite);
		
		if(!choice.equalsIgnoreCase("accept") && !choice.equalsIgnoreCase("denied")) {
			ErrorResponse error = new ErrorResponse();
			error.setTitle("Escolha inválida");
			error.setMessage("Este valor não é válido para o novo status da invite. Consulte nossa documentação.");
			error.setStatus(HttpStatus.BAD_REQUEST);
			
			return ResponseHandler.toResponseEntity(error, HttpStatus.BAD_REQUEST);
		}
		// Check if invite exist
		if(!invite.isPresent()) {
			ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND, "invite");
			return ResponseHandler.toResponseEntity(error, HttpStatus.NOT_FOUND);
		}
		
		ResponseEntity<?> projectConcluded = this.projectConcluded(invite.get().getAccess().getProject().getId());
		if(projectConcluded != null)
			return projectConcluded;
		
		// Check if user allowed
		String validationUpdateInvite = this.updateInviteValidation(request, idInvite);
		
		if(!validationUpdateInvite.equalsIgnoreCase("authorized")) {
			switch(validationUpdateInvite) {
				case "unauthorized":
					ErrorResponse errorU = new ErrorResponse(HttpStatus.FORBIDDEN, null);
					return ResponseHandler.toResponseEntity(errorU, HttpStatus.FORBIDDEN);
				
				case "bad-request":
					ErrorResponse errorB = new ErrorResponse();
					errorB.setTitle("Impossível alterar estado do convite");
					errorB.setMessage("Este convite já foi aceito ou negado, impossível nova alteração.");
					errorB.setStatus(HttpStatus.BAD_REQUEST);
					
					return ResponseHandler.toResponseEntity(errorB, HttpStatus.BAD_REQUEST);
			}
			
		}
		
		choice = choice.equalsIgnoreCase("accept")?"aceito":"recusado";
		
		invite.get().setStatus(choice);
		
		this.repository.save(invite.get());
		
		ResponseInviteDTO inviteResponse = new ResponseInviteDTO(invite.get());
		
		return ResponseHandler.toResponseEntity(inviteResponse, HttpStatus.OK);
	}
	
	public ResponseEntity<?> deleteInvite(HttpServletRequest request, Long id) {
		Optional<Invite> invite = this.repository.findById(id);
		Optional<UserListed> user = this.tokenTools.findUserByToken(request);
		
		if(!invite.isPresent()) {
			ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND, "invite");
			return ResponseHandler.toResponseEntity(error, HttpStatus.NOT_FOUND);
		}
		
		if(invite.get().getUser() != user.get()) {
			ErrorResponse error = new ErrorResponse(HttpStatus.FORBIDDEN, null);
			return ResponseHandler.toResponseEntity(error, HttpStatus.FORBIDDEN);
		}
		
		this.repository.delete(invite.get());
		
		message.put("message", "Convite apagado com sucesso");
		
		return ResponseHandler.toResponseEntity(message.toMap(), HttpStatus.OK);
	}
	
	private String validationUser(String user, Long idProject) {
		Optional<UserListed> searchUser = this.userRepository.findByUsernameOrEmail(user, user);
		
		if(!searchUser.isPresent()) {
			return "not-found";
		}
		
		List<UserListed> team = this.projectRepository.findTeamByProjectId(idProject);
		
		// Checks whether the invited user is already in the project
		if (team.contains(searchUser.get())) {				
			return "conflict";
		}
		
		return "valid"; 
	}
	
	private String updateInviteValidation(HttpServletRequest request, Long idInvite) {
		Optional<Invite> invite = this.repository.findById(idInvite);
		Optional<UserListed> user = tokenTools.findUserByToken(request);
		UserListed userAllowed = invite.get().getUser();

		if(user.get() != userAllowed)
			return "unauthorized";
		
		// Checks whether the status of the invite has not been changed
		if(!invite.get().getStatus().equalsIgnoreCase("pendente"))
			return "bad-request";
		
		return "authorized";
	}
	
	private String whatIsRole(String initialsRole) {
		switch(initialsRole) {
			case "SM":
				return "Scrum Master";
			case "DV":
				return "desenvolvedor";
			default:
				return "invalid";
		}
	}
	
	private ResponseEntity<?> projectConcluded(Long idProject) {
		Optional<Project> searchProject = this.projectRepository.findById(idProject);
		if(searchProject.get().isConcluded()) {
			ErrorResponse error = new ErrorResponse("Projeto já concluído", 
					"Impossível fazer esta ação em projetos já conclúidos", HttpStatus.CONFLICT);
			return ResponseHandler.toResponseEntity(error, HttpStatus.CONFLICT);
		}
		return null;
	}
}
