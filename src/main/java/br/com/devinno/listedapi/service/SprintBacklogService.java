package br.com.devinno.listedapi.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.devinno.listedapi.JWT.JWTTools;
import br.com.devinno.listedapi.dataTransferObject.response.ResponseSprintBacklogDTO;
import br.com.devinno.listedapi.errorMessage.ErrorResponse;
import br.com.devinno.listedapi.handler.ResponseHandler;
import br.com.devinno.listedapi.model.Backlog;
import br.com.devinno.listedapi.model.Project;
import br.com.devinno.listedapi.model.Sprint;
import br.com.devinno.listedapi.model.SprintBacklog;
import br.com.devinno.listedapi.repository.BacklogRepository;
import br.com.devinno.listedapi.repository.ProjectRepository;
import br.com.devinno.listedapi.repository.SprintBacklogRepository;
import br.com.devinno.listedapi.repository.SprintRepository;

@Service
public class SprintBacklogService {
	
	@Autowired
	private SprintBacklogRepository repository;
	
	@Autowired
	private BacklogRepository backlogRepository;
	
	@Autowired
	private SprintRepository sprintRepository;
	
	@Autowired
	private ProjectRepository projectRepository;
	
	@Autowired
	private JWTTools tokenTools;

	
	
	public ResponseEntity<?> readSprintBacklog(HttpServletRequest request, Long idProject, Long idSprint, Long idBacklog) {
		ResponseEntity<?> verification = this.verification(request, idProject, idSprint, "DEV");
		if(verification != null)
			return verification;

		Optional<SprintBacklog> sprintBacklog = this.repository.findBySprintIdAndBacklogId(idSprint, idBacklog);

		if(!sprintBacklog.isPresent()) {
			ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND, "sprintBacklog");
			return ResponseHandler.toResponseEntity(error, HttpStatus.NOT_FOUND);
		}

		Optional<Backlog> searchBacklog = this.backlogRepository.findById(idBacklog);
		Optional<SprintBacklog> searchSprintBacklog = this.repository.findBySprintIdAndBacklogId(idSprint, idBacklog);
		Project project = this.projectRepository.findById(idProject).get();
		
		// Check if backlog exists
		if(!searchBacklog.isPresent()) {
			ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND, "backlog");
			return ResponseHandler.toResponseEntity(error, HttpStatus.NOT_FOUND);
		}

		// Check that the backlog does not belong to Project
		if(searchBacklog.get().getProject() != project) {
			ErrorResponse error = new ErrorResponse("Backlog inválido","O backlog " + idBacklog + " não pertence a este projeto",HttpStatus.CONFLICT);
			return ResponseHandler.toResponseEntity(error, HttpStatus.CONFLICT);
		}

		// Check that the backlog does not belong to Sprint
		if(!searchSprintBacklog.isPresent()) {
			ErrorResponse error = new ErrorResponse("Não foi possível ler este sprint backlog", "Este backlog não está associado à esta sprint", HttpStatus.CONFLICT);
			return ResponseHandler.toResponseEntity(error, HttpStatus.CONFLICT);
		}

		ResponseSprintBacklogDTO sprintBacklogResponse = new ResponseSprintBacklogDTO(sprintBacklog.get());

		return ResponseHandler.toResponseEntity(sprintBacklogResponse, HttpStatus.OK);

	}
	
	public ResponseEntity<?> readSprintBacklogs(HttpServletRequest request, Long idProject, Long idSprint) {
		ResponseEntity<?> verification = this.verification(request, idProject, idSprint, "DEV");
		if(verification != null)
			return verification;

		List<SprintBacklog> sprintBacklogs = this.repository.findBySprintId(idSprint);
		List<ResponseSprintBacklogDTO> sprintBacklogsResponse = new ArrayList<ResponseSprintBacklogDTO>();
		
		//Check if the sprint is not associated with any backlog
		if(sprintBacklogs.isEmpty()) 
			return ResponseHandler.toResponseEntity(sprintBacklogs, HttpStatus.OK);

		// Convert to response object
		for(SprintBacklog item: sprintBacklogs) 
			sprintBacklogsResponse.add(new ResponseSprintBacklogDTO(item));

		return ResponseHandler.toResponseEntity(sprintBacklogsResponse, HttpStatus.OK);

	}
	
	public ResponseEntity<?> isConcluded(Backlog backlog) {
		// Checks if backlog is concluded
		Optional<SprintBacklog> isConcluded = this.repository.findByBacklogIdAndConcluded(backlog.getId(), true);
		if(isConcluded.isPresent()) {
			ErrorResponse error = new ErrorResponse("Não é possível adicionar este backlog à sprint",
					"Este backlog já foi concluído na Sprint com ID >" + isConcluded.get().getSprint().getId() + "<", 
					HttpStatus.CONFLICT);
			return ResponseHandler.toResponseEntity(error, HttpStatus.CONFLICT);
		}
		return null;
	}
	
	public ResponseEntity<?> verification(HttpServletRequest request, Long idProject, Long idSprint, String levelAccess) {
		// Check if project exists
		Optional<Project> searchProject= this.projectRepository.findById(idProject);
		Optional<Sprint> searchSprint = this.sprintRepository.findById(idSprint);
		if(!searchProject.isPresent()) {
			ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND, "project");
			return ResponseHandler.toResponseEntity(error, HttpStatus.NOT_FOUND);
		}
		
		if(searchProject.get().isConcluded()) {
			ErrorResponse error = new ErrorResponse("Projeto já concluído", 
					"Impossível fazer esta ação em projetos já conclúidos", HttpStatus.CONFLICT);
			return ResponseHandler.toResponseEntity(error, HttpStatus.CONFLICT);
		}
		// Check the user is allowed
		if(!tokenTools.permissionAction(request, idProject, levelAccess)) {
			ErrorResponse error = new ErrorResponse(HttpStatus.FORBIDDEN, null);
			return ResponseHandler.toResponseEntity(error, HttpStatus.FORBIDDEN);
		}
		// Check if sprint exist
		if(!searchSprint.isPresent()) {
			ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND, "sprint");
			return ResponseHandler.toResponseEntity(error, HttpStatus.NOT_FOUND);
		}

		return null;
	}
}
