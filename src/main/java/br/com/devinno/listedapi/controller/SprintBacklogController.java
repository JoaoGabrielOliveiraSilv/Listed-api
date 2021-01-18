package br.com.devinno.listedapi.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.devinno.listedapi.service.SprintBacklogService;

@RestController
@RequestMapping("/projects/{id_project}/sprints/{id_sprint}/backlogs")
public class SprintBacklogController {

	@Autowired
	private SprintBacklogService service;
	
	@GetMapping
	public ResponseEntity<?> readSprintBacklog(HttpServletRequest request, @PathVariable("id_project") Long idProject,
			@PathVariable("id_sprint") Long idSprint) {
		return this.service.readSprintBacklogs(request, idProject, idSprint);
	}
	
	@GetMapping("/{id_backlog}")
	public ResponseEntity<?> readSprintBacklog(HttpServletRequest request, @PathVariable("id_project") Long idProject,
			@PathVariable("id_sprint") Long idSprint, @PathVariable("id_backlog") Long idBacklog) {
		return this.service.readSprintBacklog(request, idProject, idSprint, idBacklog);
	}
}
