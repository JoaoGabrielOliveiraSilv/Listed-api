package br.com.devinno.listedapi.JWT;

import com.auth0.jwt.JWT;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.devinno.listedapi.errorMessage.ErrorResponse;
import br.com.devinno.listedapi.model.UserListed;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import static br.com.devinno.listedapi.JWT.SecurityConstants.EXPIRATION_TIME;
import static br.com.devinno.listedapi.JWT.SecurityConstants.SECRET_REGISTER;
import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
    private AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {
        try {
        	UserListed creds = new ObjectMapper()
                    .readValue(req.getInputStream(), UserListed.class);

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getUsername().toLowerCase(),
                            creds.getPassword(),
                            new ArrayList<>())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {

        String token = JWT.create()
                .withSubject(((User) auth.getPrincipal()).getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(HMAC512(SECRET_REGISTER.getBytes()));

        JSONObject tokenJson = new JSONObject();
        tokenJson.put("token", token);

        res.addHeader("Content-Type", "application/json");
        res.setCharacterEncoding("UTF-8");
        res.getWriter().print(tokenJson);
        res.getWriter().flush();
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException failed)
			throws IOException, ServletException {
		SecurityContextHolder.clearContext();

		ErrorResponse error = new ErrorResponse();
		error.setTitle("Credenciais inválidas");
		error.setMessage("Login e/ou senha incorretos");
		error.setStatus(HttpStatus.UNAUTHORIZED);
		
		response.setContentType("application/problem+json");
		response.setCharacterEncoding("UTF-8");
		response.setStatus(401);
		response.getWriter().write(error.toJson());
		response.getWriter().flush();
	}
}