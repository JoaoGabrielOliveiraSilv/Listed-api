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
import br.com.devinno.listedapi.dataTransferObject.request.RequestTaskDTO;
import br.com.devinno.listedapi.dataTransferObject.request.RequestUpdateTaskDTO;
import br.com.devinno.listedapi.dataTransferObject.response.ResponseTaskDTO;
import br.com.devinno.listedapi.errorMessage.ErrorResponse;
import br.com.devinno.listedapi.handler.ResponseHandler;
import br.com.devinno.listedapi.model.Access;
import br.com.devinno.listedapi.model.Backlog;
import br.com.devinno.listedapi.model.Project;
import br.com.devinno.listedapi.model.Sprint;
import br.com.devinno.listedapi.model.SprintBacklog;
import br.com.devinno.listedapi.model.Task;
import br.com.devinno.listedapi.model.UserListed;
import br.com.devinno.listedapi.repository.AccessRepository;
import br.com.devinno.listedapi.repository.BacklogRepository;
import br.com.devinno.listedapi.repository.ProjectRepository;
import br.com.devinno.listedapi.repository.SprintBacklogRepository;
import br.com.devinno.listedapi.repository.SprintRepository;
import br.com.devinno.listedapi.repository.TaskRepository;

@Service
public class TaskService {

	@Autowired
	private TaskRepository repository;
	
	@Autowired
	private BacklogRepository backlogRepository;
	
	@Autowired
	private AccessRepository accessRepository;
	
	@Autowired
	private ProjectRepository projectRepository;
	
	@Autowired
	private SprintRepository sprintRepository;
	
	@Autowired
	private SprintBacklogRepository sprintBacklogRepository;
	
	@Autowired
	private JWTTools tokenTools;
	
	private JSONObject message = new JSONObject();
	
	public ResponseEntity<?> save(HttpServletRequest request, Long idProject, Long idBacklog, RequestTaskDTO newTask) {
		// faz as verificações
		ResponseEntity<?> verificationTask = this.verification(request, idProject, idBacklog, "SM");
		// Caso haja um erro irá retorná-lo
		if(verificationTask != null) 
			return verificationTask;
		
		ResponseEntity<?> projectConcluded = this.projectConcluded(idProject);
		if(projectConcluded != null)
			return projectConcluded;
		
		Optional<Backlog> backlog =	this.backlogRepository.findById(idBacklog);
		
		Task task = new Task(newTask, backlog.get(), null);
		// Persiste no banco e configura classe de retorno
		ResponseTaskDTO taskResponse = new ResponseTaskDTO(this.repository.save(task));
		
		if(!backlog.get().getSprintBacklogs().isEmpty())
			this.concludeBacklog(backlog.get(), idProject);
		
		return ResponseHandler.toResponseEntity(taskResponse, HttpStatus.CREATED);
	}
	
	public ResponseEntity<?> readTasks(HttpServletRequest request, Long idProject, Long idBacklog) {
		// faz as verificações
		ResponseEntity<?> verificationTask = this.verification(request, idProject, idBacklog, "DEV");
		// Caso haja um erro irá retorná-lo
		if(verificationTask != null) 
			return verificationTask;
		
		// Busca as tasks e as converte em padrão de reposta
		List<Task> tasks = this.repository.findByBacklogId(idBacklog);
		List<ResponseTaskDTO> tasksResponse = new ArrayList<ResponseTaskDTO>();
		
		if(tasks.isEmpty()) 
			return ResponseHandler.toResponseEntity(tasks, HttpStatus.OK);
		
		
		for(Task item: tasks)
			tasksResponse.add(new ResponseTaskDTO(item));
		
		return ResponseHandler.toResponseEntity(tasksResponse, HttpStatus.OK);
	}
	
	public ResponseEntity<?> readTasks(HttpServletRequest request, Long idProject, Long idBacklog, Long idTask) {
		// faz as verificações
		ResponseEntity<?> verificationTask = this.verification(request, idProject, idBacklog, "DEV");
		// Caso haja um erro irá retorná-lo
		if(verificationTask != null) 
			return verificationTask;
		
		Optional<Task> task = this.repository.findById(idTask);
		Optional<Backlog> backlog = this.backlogRepository.findById(idBacklog);
		
		// Verifica se a task existe
		if(!task.isPresent()) {
			ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND, "task");
			error.setMessage(error.getMessage() + " no backlog citado");
			return ResponseHandler.toResponseEntity(error, HttpStatus.NOT_FOUND);
		}

		// Verifica se a tarefa pertence ao backlog
		if(task.get().getBacklog() != backlog.get()) {
			ErrorResponse error = new ErrorResponse("Tarefa não pertence ao backlog", 
					"Esta tarefa não pertence ao backlog inserido", HttpStatus.CONFLICT);
			return ResponseHandler.toResponseEntity(error, HttpStatus.CONFLICT);
		}

		ResponseTaskDTO taskResponse = new ResponseTaskDTO(task.get());

		return ResponseHandler.toResponseEntity(taskResponse, HttpStatus.OK);
	}
	
	public ResponseEntity<?> updateTask(HttpServletRequest request, Long idProject, Long idBacklog, Long idTask,
			RequestUpdateTaskDTO newTask) {
		// faz as verificações
		ResponseEntity<?> verificationTask = this.verification(request, idProject, idBacklog, "SM");
		// Caso haja um erro irá retorná-lo
		if(verificationTask != null) 
			return verificationTask;
		
		ResponseEntity<?> projectConcluded = this.projectConcluded(idProject);
		if(projectConcluded != null)
			return projectConcluded;
		
		Optional<Task> task = this.repository.findById(idTask);
		Optional<Backlog> backlog = this.backlogRepository.findById(idBacklog);
		
		// Verifica se a task existe
		if(!task.isPresent()) {
			ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND, "task");
			return ResponseHandler.toResponseEntity(error, HttpStatus.NOT_FOUND);
		}
		
		// Verifica se a tarefa pertence ao backlog
		if(task.get().getBacklog() != backlog.get()) {
			ErrorResponse error = new ErrorResponse("Tarefa não pertence ao backlog", 
					"Esta tarefa não pertence ao backlog inserido", HttpStatus.CONFLICT);
			return ResponseHandler.toResponseEntity(error, HttpStatus.CONFLICT);
		}
		
		if(task.get().isConcluded()) {
			ErrorResponse error = new ErrorResponse("Tarefa já concluida", 
					"Não é possível alterar uma tarefa já concluída", HttpStatus.NOT_ACCEPTABLE);
			return ResponseHandler.toResponseEntity(error, HttpStatus.NOT_ACCEPTABLE);
		}
		
		// Atualiza os dados da task, insere no banco e o converte para o padrão de resposta
		task.get().populate(newTask);
		ResponseTaskDTO taskResponse = new ResponseTaskDTO(this.repository.save(task.get()));
		
		return ResponseHandler.toResponseEntity(taskResponse, HttpStatus.OK);
	}
	
	public ResponseEntity<?> assignTask(HttpServletRequest request, Long idProject, Long idBacklog, Long idTask) {
		ResponseEntity<?> verification = this.verification(request, idProject, idBacklog, "DEV");
		Optional<Backlog> backlog = this.backlogRepository.findById(idBacklog);
		Optional<Task> task = this.repository.findById(idTask);
		Optional<UserListed> userRequest = tokenTools.findUserByToken(request);
		Optional<Project> project = this.projectRepository.findById(idProject);

		if(verification != null)
			return verification;
		
		ResponseEntity<?> projectConcluded = this.projectConcluded(idProject);
		if(projectConcluded != null)
			return projectConcluded;

		if(task.get().getAssigned() != null) {
			ErrorResponse error = new ErrorResponse("Tarefa já atribuída", 
					"Para atribuir uma tarefa ela não pode ter sido atribuída por outro membro", HttpStatus.CONFLICT);
			return ResponseHandler.toResponseEntity(error, HttpStatus.CONFLICT);
		}
		if(task.get().getBacklog() != backlog.get()) {
			ErrorResponse error = new ErrorResponse("Tarefa não pertence ao backlog", 
					"Esta tarefa não pertence ao backlog inserido", HttpStatus.CONFLICT);
			return ResponseHandler.toResponseEntity(error, HttpStatus.CONFLICT);
		}

		if(backlog.get().getSprintBacklogs().isEmpty()) {
			ErrorResponse error = new ErrorResponse("Backlog não selecionado para uma sprint", 
					"Para atribuir uma tarefa o backlog dela deve estar inserido em uma sprint", HttpStatus.CONFLICT);
			return ResponseHandler.toResponseEntity(error, HttpStatus.CONFLICT);
		}

		Optional<Access> assigned = this.accessRepository.findByUserAndProject(userRequest.get(), project.get()); 

		// Adicionar o acesso de quem foi atribuido
		task.get().setAssigned(assigned.get());
		this.repository.save(task.get());

		// Converte para o padrão de resposta
		ResponseTaskDTO taskResponse = new ResponseTaskDTO(task.get());

		return ResponseHandler.toResponseEntity(taskResponse, HttpStatus.OK);

	}
	
	public ResponseEntity<?> unassignTask(HttpServletRequest request, Long idProject, Long idBacklog, Long idTask) {
		ResponseEntity<?> verification = this.verification(request, idProject, idBacklog, "DEV");
		Optional<Backlog> backlog = this.backlogRepository.findById(idBacklog);
		Optional<Task> task = this.repository.findById(idTask);
		Optional<UserListed> userRequest = tokenTools.findUserByToken(request);

		if(verification != null)
			return verification;

		if(task.get().getBacklog() != backlog.get()) {
			ErrorResponse error = new ErrorResponse("Tarefa não pertence ao backlog", 
					"Esta tarefa não pertence ao backlog inserido", HttpStatus.CONFLICT);
			return ResponseHandler.toResponseEntity(error, HttpStatus.CONFLICT);
		}

		if(userRequest.get() != task.get().getAssigned().getUser()) {
			ErrorResponse error = new ErrorResponse(HttpStatus.FORBIDDEN, null);
			error.setMessage("Para desatribuir uma tarefa, voce deve atribuí-la primeiro");
			return ResponseHandler.toResponseEntity(error, HttpStatus.FORBIDDEN);
		}


		// Retirar o acesso de quem foi atribuido
		task.get().setAssigned(null);
		this.repository.save(task.get());

		// Converte para o padrão de resposta
		ResponseTaskDTO taskResponse = new ResponseTaskDTO(task.get());

		return ResponseHandler.toResponseEntity(taskResponse, HttpStatus.OK);
	}
	
	public ResponseEntity<?> deleteTask(HttpServletRequest request, Long idProject, Long idBacklog, Long idTask) {
		// Faz as verificações
		ResponseEntity<?> verification = this.verification(request, idProject, idBacklog, "SM");
		if(verification != null)
			return verification;

		Optional<Task> task = this.repository.findById(idTask);
		Optional<Backlog> backlog = this.backlogRepository.findById(idBacklog);
		
		if(!task.isPresent()) {
			ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND, "task");
			return ResponseHandler.toResponseEntity(error, HttpStatus.NOT_FOUND);
		}

		// Verifica se a tarefa pertence ao backlog
		if(task.get().getBacklog() != backlog.get()) {
			ErrorResponse error = new ErrorResponse("Tarefa não pertence ao backlog", 
					"Esta tarefa não pertence ao backlog inserido", HttpStatus.CONFLICT);
			return ResponseHandler.toResponseEntity(error, HttpStatus.CONFLICT);
		}

		if(task.get().isConcluded()) {
			ErrorResponse error = new ErrorResponse("Tarefa já concluida", 
					"Não é possível deletar uma tarefa já concluída", HttpStatus.NOT_ACCEPTABLE);
			return ResponseHandler.toResponseEntity(error, HttpStatus.NOT_ACCEPTABLE);
		}
		
		this.repository.deleteById(idTask);
		
		this.message.put("message", "Tarefa deletada com sucesso!");
		
		return ResponseHandler.toResponseEntity(message.toMap(), HttpStatus.OK);
	}

	public ResponseEntity<?> concludeTask(HttpServletRequest request, Long idProject, Long idBacklog, Long idTask) {
		Optional<UserListed> userRequest = this.tokenTools.findUserByToken(request);
		// Faz as verificações
		ResponseEntity<?> verification = this.verification(request, idProject, idBacklog, "DEV");
		if(verification != null)
			return verification;

		Optional<Task> task = this.repository.findById(idTask);
		Optional<Backlog> backlog = this.backlogRepository.findById(idBacklog);

		if(task.get().getAssigned() == null || userRequest.get() != task.get().getAssigned().getUser()) {
			ErrorResponse error = new ErrorResponse(HttpStatus.FORBIDDEN, null);
			error.setMessage("Você deve atribuir esta tarefa para à concluir ou desconcluir");
			return ResponseHandler.toResponseEntity(error, HttpStatus.FORBIDDEN);
		}
		
		if(!task.isPresent()) {
			ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND, "task");
			return ResponseHandler.toResponseEntity(error, HttpStatus.NOT_FOUND);
		}

		// Verifica se a tarefa pertence ao backlog
		if(task.get().getBacklog() != backlog.get()) {
			ErrorResponse error = new ErrorResponse("Tarefa não pertence ao backlog", 
					"Esta tarefa não pertence ao backlog inserido", HttpStatus.CONFLICT);
			return ResponseHandler.toResponseEntity(error, HttpStatus.CONFLICT);
		}

		// Caso a tarefa ja esteja concluida, a desconclui
		if(task.get().isConcluded()) {
			task.get().setConcluded(false);
			this.repository.save(task.get());

			this.concludeBacklog(backlog.get(), idProject);
			
			ResponseTaskDTO taskResponse = new ResponseTaskDTO(task.get());
			return ResponseHandler.toResponseEntity(taskResponse, HttpStatus.OK);
		}

		task.get().setConcluded(true);
		this.repository.save(task.get());
		
		// Verifica se deve concluir o backlog
		this.concludeBacklog(backlog.get(), idProject);

		ResponseTaskDTO taskResponse = new ResponseTaskDTO(task.get());
		
		return ResponseHandler.toResponseEntity(taskResponse, HttpStatus.OK);
	}
	
	public void concludeBacklog(Backlog backlog, Long idProject) {
		List<Sprint> sprints = this.sprintRepository.findByProjectId(idProject);
		SprintBacklog sprintBacklog = this.sprintBacklogRepository.findBySprintIdAndBacklogId(sprints.get(0).getId(), backlog.getId()).get();
		sprintBacklog.setConcluded(false);
		List<Task> tasks = this.repository.findByBacklogId(backlog.getId());
		
		boolean conclude = true;
		
		for(Task item: tasks) {
			if(!item.isConcluded()) {
				conclude = false;
			}
		}
		
		if(conclude == true && !tasks.isEmpty())
			sprintBacklog.setConcluded(true);
		else 
			sprintBacklog.setConcluded(false);

		this.sprintBacklogRepository.save(sprintBacklog);
		Sprint sprint  = sprints.get(0);
		sprint.setSprintPercentage();
		this.sprintRepository.save(sprint);
	}
	
	private ResponseEntity<?> verification(HttpServletRequest request, Long idProject, Long idBacklog, String levelAccess) {
		Optional<UserListed> userRequest = this.tokenTools.findUserByToken(request);
		// Verifica se o usuário do token existe
		if(!userRequest.isPresent()) {
			ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND, "userToken");
			return ResponseHandler.toResponseEntity(error, HttpStatus.NOT_FOUND);
		}
		
		Optional<Project> project = this.projectRepository.findById(idProject);
		// Verifica se o o projeto existe
		if(!project.isPresent()) {
			ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND, "project");
			return ResponseHandler.toResponseEntity(error, HttpStatus.NOT_FOUND);
		}
		
		// Verfica se o usuário tem permição para a ação
		if(!this.tokenTools.permissionAction(request, idProject, levelAccess)) {
			ErrorResponse error = new ErrorResponse(HttpStatus.FORBIDDEN, null);
			return ResponseHandler.toResponseEntity(error, HttpStatus.FORBIDDEN);
		}
		
		Optional<Backlog> backlog = this.backlogRepository.findById(idBacklog);
		// Verifica se o backlog existe
		if(!backlog.isPresent()) {
			ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND, "backlog");
			return ResponseHandler.toResponseEntity(error, HttpStatus.NOT_FOUND);
		}
		
		// Verifica se o backlog faz parte do projeto
		if(backlog.get().getProject() != project.get()) {
			ErrorResponse error = new ErrorResponse("Backlog inválido", "Este backlog não faz parte deste projeto", HttpStatus.CONFLICT);
			return ResponseHandler.toResponseEntity(error, HttpStatus.CONFLICT);
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
