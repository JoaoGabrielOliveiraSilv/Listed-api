package br.com.devinno.listedapi.service;

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
import br.com.devinno.listedapi.dataTransferObject.request.RequestSprintDTO;
import br.com.devinno.listedapi.dataTransferObject.request.RequestUpdateSprintDTO;
import br.com.devinno.listedapi.dataTransferObject.response.ResponseSprintDTO;
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
public class SprintService {

	@Autowired
	private SprintRepository repository;
	
	@Autowired
	private ProjectRepository projectRepository;
	
	@Autowired
	private BacklogRepository backlogRepository;
	
	@Autowired
	private SprintBacklogRepository sprintBacklogRepository;
	
	@Autowired
	private SprintBacklogService sprintBacklogService;
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private JWTTools tokenTools;
	
	private JSONObject message = new JSONObject(); 
	
	public ResponseEntity<?> save(HttpServletRequest request, Long idProject, RequestSprintDTO sprint) {//
		Optional<Project> project = this.projectRepository.findById(idProject);
		List<Backlog> backlogs = new ArrayList<Backlog>();
		List<SprintBacklog> sprintBacklogs = new ArrayList<SprintBacklog>();
		ResponseEntity<?> verification = this.verification(request, idProject, "SM");
		
		if(verification != null) {
			return verification;
		}
		
		ResponseEntity<?> projectConcluded = this.projectConcluded(idProject);
		if(projectConcluded != null)
			return projectConcluded;
		
		if(sprint.getBacklogs() != null) {
			//Checks whether the inserted backlogs are valid
			for(int i=0;i < sprint.getBacklogs().length; i++) {
				Optional<Backlog> searchBacklog = this.backlogRepository.findById(sprint.getBacklogs()[i]);
				
				// Checks whether the backlog exists
				if(!searchBacklog.isPresent()) {
					ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND, "backlog");
					error.setMessage("O backlog com o código >" + sprint.getBacklogs()[i] + "< não existe");
					return ResponseHandler.toResponseEntity(error, HttpStatus.NOT_FOUND);
				}

				if(searchBacklog.get().getProject() != project.get()) {
					ErrorResponse error = new ErrorResponse("Backlog inválido","O backlog " + sprint.getBacklogs()[i] + " não pertence a este projeto",HttpStatus.CONFLICT);
					return ResponseHandler.toResponseEntity(error, HttpStatus.CONFLICT);
				}
				
				Optional<SprintBacklog> searchBacklogConcluded = 
						this.sprintBacklogRepository.findByBacklogIdAndConcluded(searchBacklog.get().getId(), true);
				if(searchBacklogConcluded.isPresent()) {
					ErrorResponse error = new ErrorResponse("Backlog inválido","O backlog " + sprint.getBacklogs()[i] + " já foi concluído em outra sprint",HttpStatus.CONFLICT);
					return ResponseHandler.toResponseEntity(error, HttpStatus.CONFLICT);
				}
				
				backlogs.add(searchBacklog.get());
			}
		}
		Sprint sprintSave = new Sprint(sprint, project.get());
		
		for(Backlog item: backlogs) { 
			sprintBacklogs.add(new SprintBacklog(sprintSave, item));
		}		
		
		sprintSave.setSprintBacklogs(sprintBacklogs);
		sprintSave.setSprintPercentage();
		this.repository.save(sprintSave);
		this.sprintBacklogRepository.saveAll(sprintBacklogs);
		
		ResponseSprintDTO sprintResponse = new ResponseSprintDTO(sprintSave);
		
		return ResponseHandler.toResponseEntity(sprintResponse, HttpStatus.CREATED);
	}
	
	public ResponseEntity<?> readSprints(HttpServletRequest request, Long idProject) {
		
		ResponseEntity<?> verification = this.verification(request, idProject, "DEV");
		
		if(verification != null) 
			return verification;
		
		// Search all project sprints
		List<Sprint> sprints = this.repository.findByProjectId(idProject);
		List<ResponseSprintDTO> sprintsResponse = new ArrayList<ResponseSprintDTO>();
		
		if(sprints.isEmpty()) {
			return ResponseHandler.toResponseEntity(sprints, HttpStatus.OK);
		}
		
		// Convert to response object
		for(Sprint item: sprints) {
			sprintsResponse.add(new ResponseSprintDTO(item));
		}
		
		return ResponseHandler.toResponseEntity(sprintsResponse, HttpStatus.OK);
	}
	
	public ResponseEntity<?> readSprint(HttpServletRequest request, Long idProject, Long idSprint) {
		
		ResponseEntity<?> verification = this.verification(request, idProject, "DEV");
		
		if(verification != null) 
			return verification;
		
		Optional<Sprint> sprint = this.repository.findById(idSprint);
		
		if(!sprint.isPresent()) {
			ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND, "sprint");
			return ResponseHandler.toResponseEntity(error, HttpStatus.NOT_FOUND);
		}
		
		// Convert to response object
		ResponseSprintDTO sprintResponse = new ResponseSprintDTO(sprint.get());
		
		return ResponseHandler.toResponseEntity(sprintResponse, HttpStatus.OK);
	}
	
	public ResponseEntity<?> updateSprint(HttpServletRequest request, Long idProject, Long idSprint, RequestUpdateSprintDTO newSprint) {
		
		ResponseEntity<?> verification = this.verification(request, idProject, "SM");
		
		if(verification != null) 
			return verification;
		
		Optional<Sprint> sprint = this.repository.findById(idSprint);
		
		ResponseEntity<?> projectConcluded = this.projectConcluded(idProject);
		if(projectConcluded != null)
			return projectConcluded;
		
		if(!sprint.isPresent()) {
			ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND, "sprint");
			return ResponseHandler.toResponseEntity(error, HttpStatus.NOT_FOUND);
		}
		
		sprint.get().populate(newSprint);
		this.repository.save(sprint.get());
		
		// Check changes in backlog
		ResponseEntity<?> verificationBacklogs = this.sprintBacklogService.verification(request, idProject, idSprint, "PO");
		
		if(verificationBacklogs != null)
			return verificationBacklogs;

		Optional<Project> project = this.projectRepository.findById(idProject);
		List<Backlog> backlogs = new ArrayList<Backlog>();
		List<SprintBacklog> sprintBacklogs = sprint.get().getSprintBacklogs();

		if(newSprint.getBacklogs() != null) {
			// Verifica se os backlogs inseridos são válidos
			for(int i=0;i < newSprint.getBacklogs().length; i++) {
				Optional<Backlog> searchBacklog = this.backlogRepository.findById(newSprint.getBacklogs()[i]);

				// Checks if the backlog does not exist
				if(!searchBacklog.isPresent()) {
					ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND, "backlog");
					error.setMessage("O backlog com o código >" + newSprint.getBacklogs()[i] + "< não existe");
					return ResponseHandler.toResponseEntity(error, HttpStatus.NOT_FOUND);
				}
				// Checks whether the backlog does not belong to the project
				if(searchBacklog.get().getProject() != project.get()) {
					ErrorResponse error = new ErrorResponse("Backlog inválido","O backlog " + newSprint.getBacklogs()[i] + " não pertence a este projeto",HttpStatus.CONFLICT);
					return ResponseHandler.toResponseEntity(error, HttpStatus.CONFLICT);
				}

				//Checks if this backlog is not already concluded in some sprint
				ResponseEntity<?> isConcluded = this.sprintBacklogService.isConcluded(searchBacklog.get());
				if(isConcluded != null) {
					Optional<SprintBacklog> sprintBacklogConcluded = 
							this.sprintBacklogRepository.findByBacklogIdAndConcluded(searchBacklog.get().getId(), true);
					if(sprint.get() != sprintBacklogConcluded.get().getSprint())
						return isConcluded;
				}
				
				//Adds the backlog to the list to be inserted into the bank
				backlogs.add(searchBacklog.get());
			}
		}
		for(SprintBacklog item: sprintBacklogs) {
			if(item.isConcluded() && !backlogs.contains(item.getBacklog())) {
				ErrorResponse error = new ErrorResponse("Backlog já concluído", 
						"Impossível retirar o backlog com ID:" + item.getBacklog().getId() + " por já estar concluído nesta Sprint",
							HttpStatus.CONFLICT);
				return ResponseHandler.toResponseEntity(error, HttpStatus.CONFLICT);
			}
		}
		//Deletes all sprints backlogs
		this.sprintBacklogRepository.deleteAll(sprintBacklogs);
		sprintBacklogs.clear();
		
		for(Backlog item: backlogs)
			sprintBacklogs.add(new SprintBacklog(sprint.get(), item));
		
		this.sprintBacklogRepository.saveAll(sprintBacklogs);
		
		sprint.get().setSprintPercentage();
		
		this.repository.save(sprint.get());
		
		for(Backlog item: backlogs)
			this.taskService.concludeBacklog(item, idProject);
		
		// Convert to response object
		ResponseSprintDTO sprintResponse = new ResponseSprintDTO(sprint.get());
		
		return ResponseHandler.toResponseEntity(sprintResponse, HttpStatus.OK);
	}
	
	public ResponseEntity<?> deleteSprint(HttpServletRequest request, Long idProject, Long idSprint) {
		ResponseEntity<?> verification = this.verification(request, idProject, "SM");
		
		if(verification != null)
			return verification;
		
		ResponseEntity<?> projectConcluded = this.projectConcluded(idProject);
		if(projectConcluded != null)
			return projectConcluded;
		
		Optional<Sprint> sprint = this.repository.findById(idSprint);
		
		if(!sprint.isPresent()) {
			ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND, "sprint");
			return ResponseHandler.toResponseEntity(error, HttpStatus.NOT_FOUND);
		}
		
		this.sprintBacklogRepository.deleteAll(sprint.get().getSprintBacklogs());
		this.repository.delete(sprint.get());
		
		this.message.put("message", "Sprint excluída com sucesso");
		
		return ResponseHandler.toResponseEntity(message.toMap(), HttpStatus.OK);
	}
	
	public ResponseEntity<?> concludeSprint(HttpServletRequest request, Long idProject, Long idSprint) {
		ResponseEntity<?> verification = this.verification(request, idProject, "SM");

		if(verification != null)
			return verification;

		ResponseEntity<?> projectConcluded = this.projectConcluded(idProject);
		if(projectConcluded != null)
			return projectConcluded;
		
		Optional<Sprint> sprint = this.repository.findById(idSprint);

		if(!sprint.isPresent()) {
			ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND, "sprint");
			return ResponseHandler.toResponseEntity(error, HttpStatus.NOT_FOUND);
		}
		
		sprint.get().setConcluded(true);
		this.repository.save(sprint.get());
		
		ResponseSprintDTO sprintResponse = new ResponseSprintDTO(sprint.get());
		
		return ResponseHandler.toResponseEntity(sprintResponse, HttpStatus.OK);
	}
	
	private ResponseEntity<?> verification(HttpServletRequest request, Long idProject, String levelAccess) {
		//Checks whether the project exists
		Optional<Project> searchProject= this.projectRepository.findById(idProject);
		if(!searchProject.isPresent()) {
			ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND, "project");
			return ResponseHandler.toResponseEntity(error, HttpStatus.NOT_FOUND);
		}

		//Check if the user is allowed to do this action
		if(!tokenTools.permissionAction(request, idProject, levelAccess)) {
			ErrorResponse error = new ErrorResponse(HttpStatus.FORBIDDEN, null);
			return ResponseHandler.toResponseEntity(error, HttpStatus.FORBIDDEN);
		}
		
		return null;
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
