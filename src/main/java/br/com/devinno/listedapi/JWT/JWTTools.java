package br.com.devinno.listedapi.JWT;

import java.util.Date;
import java.util.List;
import java.util.Optional;


import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;

import static br.com.devinno.listedapi.JWT.SecurityConstants.EXPIRATION_TIME_RECOVER_PASSWORD;
import static br.com.devinno.listedapi.JWT.SecurityConstants.ISSUER;
import static br.com.devinno.listedapi.JWT.SecurityConstants.SECRET_RECOVER_PASSWORD;
import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

import br.com.devinno.listedapi.model.UserListed;
import br.com.devinno.listedapi.repository.ProjectRepository;
import br.com.devinno.listedapi.repository.UserListedRepository;

@Component
public class JWTTools {

	private static DecodedJWT decodedJWT;
	
	@Autowired
	private UserListedRepository userListedRepository; 
	
	@Autowired
	private ProjectRepository projectRepository;
	
	public Optional<UserListed> findUserByToken(String token) {
		JWTTools.decodedJWT = JWT.decode(token);
		
		return this.userListedRepository.findByEmail(decodedJWT.getSubject());
	}
	
	public Optional<UserListed> findUserByToken(HttpServletRequest request) {
		String token = request.getHeader("Authorization").replace("Bearer ", "");
		JWTTools.decodedJWT = JWT.decode(token);
		
		return this.userListedRepository.findByEmail(decodedJWT.getSubject());
	}
	
	public boolean permissionAction(HttpServletRequest request, Long idProject, String levelAccess) {
		Optional<UserListed> userRequest = this.findUserByToken(request);
		Optional<UserListed> productOwner = this.projectRepository.findProductOwnerByProjectId(idProject);
		Optional<UserListed> scrumMaster = this.projectRepository.findScrumMasterByProjectId(idProject);
		List<UserListed>  team = this.projectRepository.findTeamByProjectId(idProject);
	
		switch (levelAccess) {
			case "PO":
				if(userRequest.get() == productOwner.get())
					return true;
				else
					return false;
			case "SM":
					if(userRequest.get() == productOwner.get() || userRequest.get() == scrumMaster.get())
						return true;
					else
						return false;
			default:
				if(team.contains(userRequest.get()))
					return true;
				else
					return false;
		}
	}
}
