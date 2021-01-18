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

import br.com.devinno.listedapi.dataTransferObject.request.RequestSprintDTO;
import br.com.devinno.listedapi.dataTransferObject.request.RequestUpdateSprintDTO;
import br.com.devinno.listedapi.service.SprintService;

@RestController
@RequestMapping("/projects/{id_project}/sprints")
public class SprintController {

	@Autowired
	private SprintService service;
	
	@PostMapping
	public ResponseEntity<?> createSprint(HttpServletRequest request, @PathVariable("id_project") Long idProject, @Valid @RequestBody RequestSprintDTO newSprint) {
		return this.service.save(request, idProject, newSprint);
	}

	@GetMapping
	public ResponseEntity<?> readSprints(HttpServletRequest request, @PathVariable("id_project") Long idProject) {
		return this.service.readSprints(request, idProject);
	}

	@GetMapping("/{id_sprint}")
	public ResponseEntity<?> readSprint(HttpServletRequest request, @PathVariable("id_project") Long idProject, @PathVariable("id_sprint") Long idSprint) {
		return this.service.readSprint(request, idProject, idSprint);
	}
	
	@PutMapping("/{id_sprint}")
	public ResponseEntity<?> updateSprint(HttpServletRequest request, @PathVariable("id_project") Long idProject,
			@PathVariable("id_sprint") Long idSprint, @Valid @RequestBody RequestUpdateSprintDTO newSprint) {
		return this.service.updateSprint(request, idProject, idSprint, newSprint);
	}
	
	@DeleteMapping("/{id_sprint}")
	public ResponseEntity<?> deleteSprint(HttpServletRequest request, @PathVariable("id_project") Long idProject, 
			@PathVariable("id_sprint") Long idSprint) {
		return this.service.deleteSprint(request, idProject, idSprint);
	}
	
	@PatchMapping("/{id_sprint}")
	public ResponseEntity<?> concludeSprint(HttpServletRequest request, @PathVariable("id_project") Long idProject,
			@PathVariable("id_sprint") Long idSprint) {
		return this.service.concludeSprint(request, idProject, idSprint);
	}
}
