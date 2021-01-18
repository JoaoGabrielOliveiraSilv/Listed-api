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

import br.com.devinno.listedapi.dataTransferObject.request.RequestProjectDTO;
import br.com.devinno.listedapi.dataTransferObject.request.RequestUpdateProjectDTO;
import br.com.devinno.listedapi.service.ProjectService;

@RestController
@RequestMapping("/projects")
public class ProjectController {

	@Autowired
	private ProjectService service;
	
	@PostMapping
	public ResponseEntity<?> createProject(HttpServletRequest request, @Valid @RequestBody RequestProjectDTO newProject) throws Exception {
		return this.service.save(newProject, request);
	}
	
	@PostMapping("/{id_project}/quit")
	public ResponseEntity<?> quitProject(HttpServletRequest request, @PathVariable("id_project") Long idProject) {
		return this.service.quitProject(request, idProject);
	}

	@GetMapping
	public ResponseEntity<?> readProjects(HttpServletRequest request) {
		return this.service.readProjects(request);
	}
	
	@GetMapping("/{id_project}")
	public ResponseEntity<?> readProject(HttpServletRequest request, @PathVariable(name = "id_project") Long idProject) {
		return this.service.readProject(request, idProject);
	}
	
	@GetMapping("{id_project}/team")
	public ResponseEntity<?> readTeam(@PathVariable("id_project") Long idProject) {
		return this.service.findTeam(idProject);
	}
	
	@PutMapping("/{id_project}")
	public ResponseEntity<?> updateProject(HttpServletRequest request, @PathVariable(name = "id_project") Long idProject, @RequestBody RequestUpdateProjectDTO newProject) {
		return this.service.updateProject(request, idProject, newProject);
	}
	
	@PatchMapping("/{id_project}")
	public ResponseEntity<?> concludeProject(HttpServletRequest request, @PathVariable(name = "id_project") Long idProject){
		return this.service.concludedProject(request, idProject);
	}
	
	@DeleteMapping("/{id_project}")
	public ResponseEntity<?> deleteProject(HttpServletRequest request, @PathVariable(name = "id_project") Long idProject) {
		return this.service.deleteProject(request, idProject);
	}
}