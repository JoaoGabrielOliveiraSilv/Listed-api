package br.com.devinno.listedapi.JWT;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.devinno.listedapi.model.UserListed;
import br.com.devinno.listedapi.repository.UserListedRepository;

import static java.util.Collections.emptyList;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
    private UserListedRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	Optional<UserListed> user = repository.findByUsernameOrEmail(username, username);
        
        if (!user.isPresent()) {
           	throw new UsernameNotFoundException(username);
        }

        return new User(user.get().getEmail(), user.get().getPassword(), emptyList());
    }
}
