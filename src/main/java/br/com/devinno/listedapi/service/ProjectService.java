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
import br.com.devinno.listedapi.dataTransferObject.request.RequestProjectDTO;
import br.com.devinno.listedapi.dataTransferObject.request.RequestUpdateProjectDTO;
import br.com.devinno.listedapi.dataTransferObject.response.ResponseAccessDTO;
import br.com.devinno.listedapi.dataTransferObject.response.ResponseProjectDTO;
import br.com.devinno.listedapi.errorMessage.ErrorResponse;
import br.com.devinno.listedapi.handler.ResponseHandler;
import br.com.devinno.listedapi.model.Access;
import br.com.devinno.listedapi.model.Backlog;
import br.com.devinno.listedapi.model.Category;
import br.com.devinno.listedapi.model.Invite;
import br.com.devinno.listedapi.model.Project;
import br.com.devinno.listedapi.model.Sprint;
import br.com.devinno.listedapi.model.Task;
import br.com.devinno.listedapi.model.UserListed;
import br.com.devinno.listedapi.repository.AccessRepository;
import br.com.devinno.listedapi.repository.BacklogRepository;
import br.com.devinno.listedapi.repository.CategoryRepository;
import br.com.devinno.listedapi.repository.InviteRepository;
import br.com.devinno.listedapi.repository.ProjectRepository;
import br.com.devinno.listedapi.repository.SprintBacklogRepository;
import br.com.devinno.listedapi.repository.SprintRepository;
import br.com.devinno.listedapi.repository.TaskRepository;

@Service
public class ProjectService {

	@Autowired
	private ProjectRepository repository;

	@Autowired
	private AccessRepository accessRepository;
	
	@Autowired
	private BacklogRepository backlogRepository;
	
	@Autowired
	private TaskRepository taskRepository;
	
	@Autowired
	private SprintRepository sprintRepository;
	
	@Autowired
	private SprintBacklogRepository sprintBacklogRepository;
	
	@Autowired
	private InviteRepository inviteRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private AccessService accessService;
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private JWTTools tokenTools;
	
	private JSONObject message = new JSONObject();

	public ResponseEntity<?> save(RequestProjectDTO newProject, HttpServletRequest request) {
		Optional<UserListed> userRequest = this.tokenTools.findUserByToken(request);
		
		if(!userRequest.isPresent()) { 	
			ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND, "userToken");
			return ResponseHandler.toResponseEntity(error, HttpStatus.NOT_FOUND);
		}
		
		Project project = new Project(newProject);

		Project projectSave = this.repository.save(project);

		ResponseEntity<?> accessResponse = accessService.save(project, "product-owner", userRequest.get());
		if(accessResponse.getStatusCode() != HttpStatus.CREATED)
			return accessResponse;
		
		// Convert to response object
		ResponseProjectDTO projectResponse = new ResponseProjectDTO(projectSave, "Product Owner");

		return ResponseHandler.toResponseEntity(projectResponse, HttpStatus.CREATED);
	}
	
	public ResponseEntity<?> quitProject(HttpServletRequest request, Long idProject) {
		// Check project exists
		if(!this.projectExists(idProject)) {
			ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND, "project");
			return ResponseHandler.toResponseEntity(error, HttpStatus.NOT_FOUND);
		}
		
		ResponseEntity<?> projectConcluded = this.projectConcluded(idProject);
		if(projectConcluded != null)
			return projectConcluded;
		
		Optional<UserListed> user = this.tokenTools.findUserByToken(request);
		if(!user.isPresent()) {
			ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND, "userToken");
			return ResponseHandler.toResponseEntity(error, HttpStatus.NOT_FOUND);
		}
		
		if(this.tokenTools.permissionAction(request, idProject, "PO")) {
			ErrorResponse error = new ErrorResponse("Impossível sair do projeto", 
					"Não é possível sair do projeto sendo PO", HttpStatus.CONFLICT);
			return ResponseHandler.toResponseEntity(error, HttpStatus.CONFLICT);
		}
		
		Optional<Project> project = this.repository.findById(idProject);
		Optional<Access> access = this.accessRepository.findByUserAndProject(user.get(), project.get());
		
		if(!access.isPresent()) {
			ErrorResponse error = new ErrorResponse("Não é possível sair deste projeto", "Você não está participando deste projeto", HttpStatus.CONFLICT);
			return ResponseHandler.toResponseEntity(error, HttpStatus.CONFLICT);
		}
		
		List<Task> tasks = this.taskRepository.findByAssigned(access.get());
		
		if(!tasks.isEmpty()) {
			for(Task item: tasks)
			this.taskService.unassignTask(request, idProject, item.getBacklog().getId(), item.getId());
		}
		
		//Remove the user from the project
		this.inviteRepository.deleteAllByAccess(access.get());
		this.accessRepository.delete(access.get());
		
		this.message.put("message", "Saída do projeto feita com sucesso");
		return ResponseHandler.toResponseEntity(message.toMap(), HttpStatus.OK);
	}

	public ResponseEntity<?> readProjects(HttpServletRequest request) {
		
		Optional<UserListed> user = this.tokenTools.findUserByToken(request);
		
		if(!user.isPresent()) {
			ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND, "userToken");
			return ResponseHandler.toResponseEntity(error, HttpStatus.NOT_FOUND);
		}
		
		List<Project> projects = this.repository.findByUser(user.get());
		List<ResponseProjectDTO> listProject = new ArrayList<>();
		
		if(projects.isEmpty()) {
			return ResponseHandler.toResponseEntity(projects, HttpStatus.OK);
		}
		
		// Convert to response object and add in the list
		for(Project item: projects) {
			listProject.add(new ResponseProjectDTO(item, user.get()));
		}

		return ResponseHandler.toResponseEntity(listProject, HttpStatus.OK);
	}
	
	public ResponseEntity<?> readProject(HttpServletRequest request, Long idProject) {
		Optional<UserListed> userRequest = this.tokenTools.findUserByToken(request);
		
		if(!userRequest.isPresent()) {
			ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND, "userToken");
			return ResponseHandler.toResponseEntity(error, HttpStatus.NOT_FOUND);
		}
		
		if(!this.projectExists(idProject)) {
			ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND, "project");
			return ResponseHandler.toResponseEntity(error, HttpStatus.NOT_FOUND);
		}
		
		Optional<Project> project = this.repository.findById(idProject);
		
		// Convert to response object
		ResponseProjectDTO projectResponse = new ResponseProjectDTO(project.get(), userRequest.get());

		return ResponseHandler.toResponseEntity(projectResponse, HttpStatus.OK);
	}
	
	public ResponseEntity<?> updateProject(HttpServletRequest request, Long idProject, RequestUpdateProjectDTO newProject) {

		if(!this.projectExists(idProject)) {
			ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND, "project");
			return ResponseHandler.toResponseEntity(error, HttpStatus.NOT_FOUND);
		}
		
		ResponseEntity<?> projectConcluded = this.projectConcluded(idProject);
		if(projectConcluded != null)
			return projectConcluded;

		Optional<Project> project = this.repository.findById(idProject);
		
		if(project.get().isConcluded()) {
			ErrorResponse error = new ErrorResponse();
			error.setTitle("Projeto já concluido");
			error.setMessage("Este projeto já foi concluido. Logo, não pode mais sofrer alterações!");
			error.setStatus(HttpStatus.NOT_ACCEPTABLE);
			
			return ResponseHandler.toResponseEntity(error, HttpStatus.NOT_ACCEPTABLE);
		}

		// Check if user is allowed
		if(this.tokenTools.permissionAction(request, idProject, "PO")) {			
			project.get().populate(newProject);

			this.repository.save(project.get());

			Optional<UserListed> userRequest = this.tokenTools.findUserByToken(request);
			ResponseProjectDTO projectResponse = new ResponseProjectDTO(project.get(), userRequest.get());
			
			return ResponseHandler.toResponseEntity(projectResponse, HttpStatus.OK);
		}

		ErrorResponse error = new ErrorResponse(HttpStatus.FORBIDDEN, null);

		return ResponseHandler.toResponseEntity(error, HttpStatus.FORBIDDEN);
	}
	
	public ResponseEntity<?> concludedProject(HttpServletRequest request, Long idProject) {
		if(!this.projectExists(idProject)) {
			ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND, "project");
			return ResponseHandler.toResponseEntity(error, HttpStatus.NOT_FOUND);
		}
		
		ResponseEntity<?> projectConcluded = this.projectConcluded(idProject);
		if(projectConcluded != null)
			return projectConcluded;
		
		// Check if user is allowed
		if(!this.tokenTools.permissionAction(request, idProject, "PO")) {
			ErrorResponse error = new ErrorResponse(HttpStatus.FORBIDDEN, null);
			return ResponseHandler.toResponseEntity(error, HttpStatus.FORBIDDEN);
		}
		
		Project project = this.repository.findById(idProject).get();
		
		if(project.isConcluded()) {
			ErrorResponse error = new ErrorResponse("Projeto já concluído", "Não é possível concluir um projeto já concluído", HttpStatus.CONFLICT);
			return ResponseHandler.toResponseEntity(error, HttpStatus.CONFLICT);
		}

		project.setConcluded(true);
		project.setDateEnd(LocalDate.now());

		this.repository.save(project);
		return ResponseHandler.toResponseEntity(project, HttpStatus.OK);
	}
	
	public ResponseEntity<?> deleteProject(HttpServletRequest request, Long idProject) {
		if(!this.projectExists(idProject)) {
			ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND, "project");
			return ResponseHandler.toResponseEntity(error, HttpStatus.NOT_FOUND);
		}
		
		if(!this.tokenTools.permissionAction(request, idProject, "PO")) {
			ErrorResponse error = new ErrorResponse(HttpStatus.FORBIDDEN, null);
			return ResponseHandler.toResponseEntity(error, HttpStatus.FORBIDDEN);
		}
		
		Optional<Project> project = this.repository.findById(idProject);
		List<Access> access = this.accessRepository.findByProject(project.get());

		List<Invite> invites = new ArrayList<>();	
		for(Access item: access) {
			invites.addAll(this.inviteRepository.findByAccess(item));
		}
		
		List<Sprint> sprints = this.sprintRepository.findByProjectId(idProject);
		List<Backlog> backlogs = this.backlogRepository.findByProjectId(idProject);

		for(Backlog backlog: backlogs) {
			List<Task> tasks = this.taskRepository.findByBacklogId(backlog.getId());
			this.taskRepository.deleteAll(tasks);
		}
		
		for(Sprint sprint: sprints)
			this.sprintBacklogRepository.deleteAll(sprint.getSprintBacklogs());
		
		this.sprintRepository.deleteAll(sprints);
		this.backlogRepository.deleteAll(backlogs);
		this.inviteRepository.deleteAll(invites);
		this.accessRepository.deleteAll(access);
		this.repository.delete(project.get());
		
		this.message.put("message", "Projeto excluido com sucesso");
		
		return ResponseHandler.toResponseEntity(message.toMap(), HttpStatus.OK);
	}
	
	public ResponseEntity<?> findTeam(Long idProject) {
		// Check if project exist
		if(!this.projectExists(idProject)) {
			ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND, "project");
			return ResponseHandler.toResponseEntity(error, HttpStatus.NOT_FOUND);
		}
		
		List<UserListed> team = this.repository.findTeamByProjectId(idProject);
		List<ResponseAccessDTO> teamResponse = new ArrayList<ResponseAccessDTO>();
		Optional<Project> project = this.repository.findById(idProject);
		
		for(UserListed item: team) {
			Optional<Category> category = this.categoryRepository.findByUsernameAndProjectId(item.getUsername(), idProject);
			
			teamResponse.add(new ResponseAccessDTO(item, category.get(), project.get()));
		}
		
		return ResponseHandler.toResponseEntity(teamResponse, HttpStatus.OK);
	}
	
	private boolean projectExists(Long idProject) {
		Optional<Project> searchProject = this.repository.findById(idProject);

		if(!searchProject.isPresent()) {
			return false;
		}
		
		return true;
	}
	
	private ResponseEntity<?> projectConcluded(Long idProject) {
		Optional<Project> searchProject = this.repository.findById(idProject);
		if(searchProject.get().isConcluded()) {
			ErrorResponse error = new ErrorResponse("Projeto já concluído", 
					"Impossível fazer esta ação em projetos já conclúidos", HttpStatus.CONFLICT);
			return ResponseHandler.toResponseEntity(error, HttpStatus.CONFLICT);
		}
		return null;
	}
}
