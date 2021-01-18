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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.devinno.listedapi.dataTransferObject.request.RequestBacklogDTO;
import br.com.devinno.listedapi.dataTransferObject.request.RequestUpdateBacklogDTO;
import br.com.devinno.listedapi.service.BacklogService;

@RestController
@RequestMapping("/projects/{id_project}/backlogs")
public class BacklogController {

	@Autowired
	private BacklogService service;
	
	@PostMapping
	public ResponseEntity<?> createBacklog(HttpServletRequest request, @Valid @RequestBody RequestBacklogDTO newBacklog, @PathVariable("id_project") Long idProject) {
		return this.service.save(request, newBacklog, idProject);
	}
	
	@GetMapping
	public ResponseEntity<?> readBacklogs(HttpServletRequest request, @PathVariable("id_project") Long idProject) {
		return this.service.readBacklogs(request, idProject);
	}
	
	@GetMapping("/{id_backlog}")
	public ResponseEntity<?> readBacklog(HttpServletRequest request, @PathVariable("id_project") Long idProject, @PathVariable("id_backlog") Long idBacklog) {
		return this.service.readBacklog(request, idProject, idBacklog);
	}
	
	@PutMapping("/{id_backlog}")
	public ResponseEntity<?> updateBacklog(HttpServletRequest request, @PathVariable("id_project") Long idProject, 
			@PathVariable("id_backlog") Long idBacklog, @Valid @RequestBody RequestUpdateBacklogDTO newBacklog) {
		return this.service.updateBacklog(request, idProject, idBacklog, newBacklog);
	}
	
	@DeleteMapping("/{id_backlog}")
	public ResponseEntity<?> deleteBacklog(HttpServletRequest request, @PathVariable("id_project") Long idProject, @PathVariable("id_backlog") Long idBacklog) {
		return this.service.deleteBacklog(request, idProject, idBacklog);
	}
}
