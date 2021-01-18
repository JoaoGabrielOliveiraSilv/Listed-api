package br.com.devinno.listedapi.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.devinno.listedapi.errorMessage.ErrorResponse;
import br.com.devinno.listedapi.handler.ResponseHandler;
import br.com.devinno.listedapi.model.Access;
import br.com.devinno.listedapi.model.Category;
import br.com.devinno.listedapi.model.Project;
import br.com.devinno.listedapi.model.UserListed;
import br.com.devinno.listedapi.repository.AccessRepository;
import br.com.devinno.listedapi.repository.CategoryRepository;

@Service
public class AccessService {

	@Autowired
	private AccessRepository repository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	private Optional<Category> categoryModel;
	
	public ResponseEntity<?> save(Project project, String category, UserListed user) {
		
		switch (category) {
			case "product-owner":
				this.categoryModel = this.categoryRepository.findById(1L);
			break;
			case "scrumaster":
				this.categoryModel = this.categoryRepository.findById(2L);
			break;
			case "dev-team":
				this.categoryModel = this.categoryRepository.findById(3L);
			break;
		}
		
		if(!this.categoryModel.isPresent()) {
			ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND, "category");
			return ResponseHandler.toResponseEntity(error, HttpStatus.NOT_FOUND);
		} else {
			Access access = new Access();
			access.setProject(project);
			access.setUser(user);
			access.setCategory(this.categoryModel.get());
			
			this.repository.save(access);
		}
		
		return ResponseHandler.toResponseEntity(this.repository.findByUserAndProject(user, project).get(), HttpStatus.CREATED);
	}
}
