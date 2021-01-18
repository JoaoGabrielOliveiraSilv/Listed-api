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
import br.com.devinno.listedapi.dataTransferObject.request.RequestBacklogDTO;
import br.com.devinno.listedapi.dataTransferObject.request.RequestUpdateBacklogDTO;
import br.com.devinno.listedapi.dataTransferObject.response.ResponseBacklogDTO;
import br.com.devinno.listedapi.errorMessage.ErrorResponse;
import br.com.devinno.listedapi.handler.ResponseHandler;
import br.com.devinno.listedapi.model.Backlog;
import br.com.devinno.listedapi.model.Project;
import br.com.devinno.listedapi.model.SprintBacklog;
import br.com.devinno.listedapi.model.Task;
import br.com.devinno.listedapi.repository.BacklogRepository;
import br.com.devinno.listedapi.repository.ProjectRepository;
import br.com.devinno.listedapi.repository.SprintBacklogRepository;
import br.com.devinno.listedapi.repository.TaskRepository;

@Service
public class BacklogService {

	@Autowired
	private BacklogRepository repository;
	
	@Autowired
	private ProjectRepository projectRepository; 
	
	@Autowired
	private SprintBacklogRepository sprintBacklogRepository;
	
	@Autowired
	private TaskRepository taskRepository;
	
	@Autowired
	private JWTTools tokenTools;
	
	private JSONObject message = new JSONObject();
	
	public ResponseEntity<?> save(HttpServletRequest request, RequestBacklogDTO newBacklog, Long idProject) {
		ResponseEntity<?> verification = this.verification(request, idProject, "SM");
		if(verification != null)
			return verification;
		
		Optional<Project> project = this.projectRepository.findById(idProject);
		
		Backlog backlog = new Backlog(newBacklog, project.get());
		
		this.repository.save(backlog);
		
		ResponseBacklogDTO backlogResponse = new ResponseBacklogDTO(backlog);
		
		return ResponseHandler.toResponseEntity(backlogResponse, HttpStatus.CREATED);
	}
	
	public ResponseEntity<?> readBacklogs(HttpServletRequest request, Long idProject) {
		ResponseEntity<?> verification = this.verification(request, idProject, "DEV");
		if(verification != null)
			return verification;

		List<Backlog> backlogs = this.repository.findByProjectId(idProject);
		
		if(backlogs.isEmpty()) {
			return ResponseHandler.toResponseEntity(backlogs, HttpStatus.OK);
		}
		
		List<ResponseBacklogDTO> responseBacklogs = new ArrayList<ResponseBacklogDTO>();
		
		for(Backlog item: backlogs) {
			responseBacklogs.add(new ResponseBacklogDTO(item));
		}
		
		return ResponseHandler.toResponseEntity(responseBacklogs, HttpStatus.OK);
	}
	
	public ResponseEntity<?> readBacklog(HttpServletRequest request, Long idProject, Long idBacklog) {
		ResponseEntity<?> verification = this.verification(request, idProject, "DEV");
		if(verification != null)
			return verification;
		
		Optional<Backlog> backlog = this.repository.findById(idBacklog); 
		
		if(!backlog.isPresent()) {
			ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND, "backlog");
			return ResponseHandler.toResponseEntity(error, HttpStatus.NOT_FOUND);
		}
		
		Optional<Project> project = this.projectRepository.findById(idProject);
		
		// Check if the backlog belongs to the project
		if(backlog.get().getProject() != project.get()) {
			ErrorResponse error = new ErrorResponse("Não foi possível acessar este backlog", "O backlog mencionado não faz parte deste projeto", HttpStatus.NON_AUTHORITATIVE_INFORMATION);
			return ResponseHandler.toResponseEntity(error, HttpStatus.NON_AUTHORITATIVE_INFORMATION);
		}
		
		ResponseBacklogDTO responseBacklog = new ResponseBacklogDTO(backlog.get());
		
		return ResponseHandler.toResponseEntity(responseBacklog, HttpStatus.OK);
	}
	
	public ResponseEntity<?> updateBacklog(HttpServletRequest request, Long idProject, Long idBacklog, RequestUpdateBacklogDTO newBacklog) {
		Optional<SprintBacklog> isConcluded = this.sprintBacklogRepository.findByBacklogIdAndConcluded(idBacklog, true);

		ResponseEntity<?> verification = this.verification(request, idProject, "SM");
		if(verification != null)
			return verification;
		
		ResponseEntity<?> projectConcluded = this.projectConcluded(idProject);
		if(projectConcluded != null) 
			return projectConcluded;
		
	
		Optional<Backlog> backlog = this.repository.findById(idBacklog);
		
		if(!backlog.isPresent()) {
			ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND, "backlog");
			return ResponseHandler.toResponseEntity(error, HttpStatus.NOT_FOUND);
		}
		
		if(isConcluded.isPresent()) {
			ErrorResponse error = new ErrorResponse("Backlog já concluido", "Não é possível alterar um backlog ja concluído", HttpStatus.NOT_ACCEPTABLE);
			return ResponseHandler.toResponseEntity(error, HttpStatus.NOT_ACCEPTABLE);
		}
		
		backlog.get().populate(newBacklog);
		
		this.repository.save(backlog.get());
		
		ResponseBacklogDTO backlogResponse = new ResponseBacklogDTO(backlog.get());
		
		return ResponseHandler.toResponseEntity(backlogResponse, HttpStatus.OK);
	}
	
	public ResponseEntity<?> deleteBacklog(HttpServletRequest request, Long idProject, Long idBacklog) {
		Optional<SprintBacklog> isConcluded = this.sprintBacklogRepository.findByBacklogIdAndConcluded(idBacklog, true);

		ResponseEntity<?> verification = this.verification(request, idProject, "SM");
		if(verification != null)
			return verification;
		
		ResponseEntity<?> projectConcluded = this.projectConcluded(idProject);
		if(projectConcluded != null) 
			return projectConcluded;

		Optional<Backlog> backlog = this.repository.findById(idBacklog);
		
		if(!backlog.isPresent()) {
			ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND, "backlog");
			return ResponseHandler.toResponseEntity(error, HttpStatus.NOT_FOUND);
		}
		
		if(isConcluded.isPresent()) {
			ErrorResponse error = new ErrorResponse("Backlog já concluido", 
					"Não é possível deletar uma backlog já concluído", HttpStatus.NOT_ACCEPTABLE);
			return ResponseHandler.toResponseEntity(error, HttpStatus.NOT_ACCEPTABLE);
		}
		
		List<Task> tasks = this.taskRepository.findByBacklogId(idBacklog);
		this.taskRepository.deleteAll(tasks);
		this.repository.deleteById(idBacklog);
		
		this.message.put("message", "Backlog deletado com sucesso!");
		
		return ResponseHandler.toResponseEntity(message.toMap(), HttpStatus.OK);
	}
	
	private ResponseEntity<?> verification(HttpServletRequest request, Long idProject, String levelAccess) {
		// Check if project exists
		if(!projectRepository.findById(idProject).isPresent()) {
			ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND, "project");
			return ResponseHandler.toResponseEntity(error, HttpStatus.NOT_FOUND);
		}

		// Check if user allowed
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
