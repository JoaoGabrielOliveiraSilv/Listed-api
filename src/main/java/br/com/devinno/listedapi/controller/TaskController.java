package br.com.devinno.listedapi.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.devinno.listedapi.dataTransferObject.request.RequestTaskDTO;
import br.com.devinno.listedapi.dataTransferObject.request.RequestUpdateTaskDTO;
import br.com.devinno.listedapi.service.TaskService;

@RestController
@RequestMapping("/projects/{id_project}/backlogs/{id_backlog}/tasks")
public class TaskController {

	@Autowired
	private TaskService service;
	
	@PostMapping
	public ResponseEntity<?> createTask(HttpServletRequest request, @PathVariable("id_project") Long idProject, 
			@PathVariable("id_backlog") Long idBacklog, @Valid @RequestBody RequestTaskDTO newTask) {
		return this.service.save(request, idProject, idBacklog, newTask);
	}
	
	@GetMapping
	public ResponseEntity<?> readTasks(HttpServletRequest request, @PathVariable("id_project") Long idProject,
			@PathVariable("id_backlog") Long idBacklog) {
		return this.service.readTasks(request, idProject, idBacklog);
	}
	
	@GetMapping("/{id_task}")
	public ResponseEntity<?> readTask(HttpServletRequest request, @PathVariable("id_project") Long idProject,
			@PathVariable("id_backlog") Long idBacklog, @PathVariable("id_task") Long idTask) {
			return this.service.readTasks(request, idProject, idBacklog, idTask);
	}
	
	@PutMapping("/{id_task}")
	public ResponseEntity<?> updateTask(HttpServletRequest request, @PathVariable("id_project") Long idProject,
			@PathVariable("id_backlog") Long idBacklog, @PathVariable("id_task") Long idTask,
				@Valid @RequestBody RequestUpdateTaskDTO newTask) {
		return this.service.updateTask(request, idProject, idBacklog, idTask, newTask);
	}

	@PatchMapping("/{id_task}")
	public ResponseEntity<?> concludeTask(HttpServletRequest request, @PathVariable("id_project") Long idProject,
										  @PathVariable("id_backlog") Long idBacklog, @PathVariable("id_task") Long idTask) {
		return this.service.concludeTask(request, idProject, idBacklog, idTask);
	}
	
	@DeleteMapping("/{id_task}")
	public ResponseEntity<?> deleteTask(HttpServletRequest request, @PathVariable("id_project") Long idProject,
			@PathVariable("id_backlog") Long idBacklog, @PathVariable("id_task") Long idTask) {
		return this.service.deleteTask(request, idProject, idBacklog, idTask);
	}

	@PostMapping("/{id_task}/assignment")
	public ResponseEntity<?> assignTask(HttpServletRequest request, @PathVariable("id_project") Long idProject, 
			@PathVariable("id_backlog") Long idBacklog, @PathVariable("id_task") Long idTask){
		return this.service.assignTask(request, idProject, idBacklog, idTask);
	}
	
	@DeleteMapping("/{id_task}/assignment")
	public ResponseEntity<?> unassignTask(HttpServletRequest request, @PathVariable("id_project") Long idProject, 
			@PathVariable("id_backlog") Long idBacklog, @PathVariable("id_task") Long idTask){
		return this.service.unassignTask(request, idProject, idBacklog, idTask);
	}
}
